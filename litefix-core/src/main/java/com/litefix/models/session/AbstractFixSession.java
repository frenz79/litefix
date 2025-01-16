package com.litefix.models.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.transport.ITransport;
import com.litefix.modules.transport.ITransportListener;

public abstract class AbstractFixSession implements ITransportListener{

	Logger LOGGER_MSG = LogManager.getLogger("SESSION_MESSAGES");
	Logger LOGGER_SESSION = LogManager.getLogger("SESSION");

	final SessionStateMachine sessionSM;

	final ClientFixSessionConfig sessionConfig;
	
	private long lastGapFillMessageWaitTime = 10_000l;
	private long lastGapFillMessageSendTime = 0l;
	
	private Map<String,IFixMessageListener> msgListeners = new HashMap<>();
	private IFixSessionListener sessionListener;
	private IRetransmissionInterceptor retransmissionInterceptor;

	private final IPersistence<FixMessageEncoder> persistence;
	private final ITransport transport;
	private final FixMessageMapper fixMessageMapper;
	private final IFixMessageValidator fixMessageValidator;
	
	private final ReadWriteLock persistenceOutLock = new ReentrantReadWriteLock(false);
	private final Lock rOutLock = persistenceOutLock.readLock();
	private final Lock wOutLock = persistenceOutLock.writeLock();
	
	private final void acquireLock( Lock lock ) {
		try {
			if (!lock.tryLock(500, TimeUnit.MILLISECONDS) ) {
				throw new RuntimeException(String.format("Thread:%s cannot acquire lock:'%s'. Deadlock?", Thread.currentThread(),lock));
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(String.format("Cannot acquire lock:'%s'. InterruptedException?",lock));
		}
	}
	
	public AbstractFixSession(ITransport transport, IPersistence<FixMessageEncoder> persistence, ClientFixSessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
		this.transport = transport;
		this.persistence = persistence;
		this.sessionSM = new SessionStateMachine();
		this.fixMessageMapper = new FixMessageMapper();
		this.fixMessageValidator = new FixMessageValidator( sessionConfig );
	}

	public IFixMessageListener getMessageListener( String msgType ) {
		IFixMessageListener l = msgListeners.get(msgType);
		if ( l==null ) {
			l = msgListeners.get("*");
		}
		return l;
	}
	
	private boolean isSeqGapDetected(FixMessageDecoder decoder) throws SessionRejectMessageException {
		int lastRcvMessageSeq = persistence.getLastIncomingSeq();
		
		if ( lastRcvMessageSeq>=0 ) {
			int expectedIncomingSeqNum = lastRcvMessageSeq+1;
			if ( expectedIncomingSeqNum>decoder.getSeqNum() ) {
				Boolean PossDupFlag = decoder.asBoolean(43);
				if ( Boolean.TRUE.equals(PossDupFlag) ) {
					LOGGER_SESSION.warn("Old or duplicated message detected, discarding message. Expecting:{} but received:{}",
						expectedIncomingSeqNum,
						decoder.getSeqNum()
					);
					return true;
				}
				throw new SessionRejectMessageException(
						decoder.getSeqNum(),
						35,
						decoder.getMsgType(),
						SESSION_REJECT_REASON.INVALID_MSGTYPE_35,
						String.format("MsgSeqNum too low, expecting %d but received %d",expectedIncomingSeqNum,decoder.getSeqNum()));				
			} else if ( expectedIncomingSeqNum<decoder.getSeqNum() ) {
				LOGGER_SESSION.warn("Gap detected, discarding message. Expecting:{} but received:{}",
					expectedIncomingSeqNum,
					decoder.getSeqNum()
				);
				long now = System.currentTimeMillis();
				if (now-lastGapFillMessageSendTime>lastGapFillMessageWaitTime) {
					LOGGER_SESSION.info("sending ResendRequest FROM:{} TO: 0", expectedIncomingSeqNum );
					sendMessage( buildGapFillMessage( expectedIncomingSeqNum, 0 ));
					lastGapFillMessageSendTime = now;
				} else {
					LOGGER_SESSION.warn("Gap detected. Already sent ResendRequest FROM:{} TO:{}. Not sending another.",
						expectedIncomingSeqNum,
						decoder.getSeqNum()
					);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onMessage( byte[] buffer, int from, int len, long rcvNanoTime ) {
		try {
			if ( fixMessageValidator.isGarbled(buffer, from, len) ) {
				LOGGER_MSG.trace("Discarding garbled msg: {}", new String(buffer, from, len) );
				return;
			}
			
			LOGGER_MSG.info("RCV: {}", new String(buffer, from, len) );
	
			FixMessageDecoder decoder = newDecoder( buffer, from, len, rcvNanoTime );
			fixMessageValidator.isValidMessage( decoder, decoder.getMsgType() );
						
			if ( !sessionSM.isLoggedOn() ) {			
				switch(decoder.getMsgType()) {
				case "A": 
					if ( isSeqGapDetected( decoder ) ) {
						return;
					}
					handleLogonResp(decoder); 
					persistence.incLastIncomingSeq();
					break;
				case "5": 
					handleLogout(decoder); 
					break;
				default:
					throw new SessionRejectMessageException(
							decoder.getSeqNum(),
							35,
							decoder.getMsgType(),
							SESSION_REJECT_REASON.INVALID_MSGTYPE_35,
							String.format("Only msgType 'A' or '5' supported when not logged in.Discarded msgType:%s",decoder.getMsgType()));
				}
			} else {				
				handleApplicativeMessage(decoder);
			}
		} catch( BusinessRejectMessageException ex1 ) {
			sendMessage( buildBusinessRejectMessage(ex1));
		} catch( SessionRejectMessageException ex2 ) {	
			sendMessage( buildRejectMessage(ex2) );
			sendMessage( buildLogoutMessage(ex2) );
			disconnect();			
		} catch (Exception e) {
			LOGGER_SESSION.error("Exception Handled in onMessage().",e);	
		}
	}
	
	private void handleApplicativeMessage( FixMessageDecoder decoder ) throws SessionRejectMessageException, BusinessRejectMessageException {
		if ( "4".equals(decoder.getMsgType())) {
			Integer NewSeqNo = decoder.asInt(36);
			Boolean GapFillFlag = decoder.asBoolean(123);
			if ( Boolean.TRUE.equals(GapFillFlag) ) {
				// Gap fill
				LOGGER_SESSION.info("got GapFill to:{}", NewSeqNo);
				resetIncomingSequence( NewSeqNo );
			} else {
				// Reset mode
				LOGGER_SESSION.info("got SequenceReset to:{}", NewSeqNo);
				resetIncomingSequence( NewSeqNo );					
			}				
			return;
		}
		
		if ( isSeqGapDetected( decoder ) ) {
			return;
		}
		
		persistence.incLastIncomingSeq();
		
		switch(decoder.getMsgType()) {
		case "0": handleHeartbeat(decoder); break;
		case "1": handleTestRequest(decoder); break;
		case "2": handleGapFillRequest(decoder); break;
		default:
			IFixMessageListener listener = getMessageListener(decoder.getMsgType());
			if ( listener!=null ) {
				listener.onMessageRcv(decoder);
			} else {
				throw new SessionRejectMessageException(
						decoder.getSeqNum(),
						35,
						decoder.getMsgType(),
						SESSION_REJECT_REASON.INVALID_MSGTYPE_35,
						String.format("Unsupported msgType:%s",decoder.getMsgType()));
			}
			break;
		}
	}

	// 35=A
	private void handleLogonResp(FixMessageDecoder decoder) throws SessionRejectMessageException, BusinessRejectMessageException {
		sessionSM.logon(true);
		getSessionListener().onLogon(decoder, true);
	}
	// 35=5
	private void handleLogout(FixMessageDecoder decoder) {
		sessionSM.logon(false);
		getSessionListener().onLogout(decoder);
	}
	// 35=0
	private void handleHeartbeat(FixMessageDecoder decoder) {
		// TODO: code me
	}
	// 35=1
	private void handleTestRequest(FixMessageDecoder decoder) {
		FixMessageEncoder enc = buildHeartbeatMessage( decoder.asString(112) );
		sendMessage( enc );
	}
	// 35=2
	private void handleGapFillRequest(FixMessageDecoder decoder) throws SessionRejectMessageException {
		int BeginSeqNo = -1;
		int EndSeqNo = -1;
		try {
			BeginSeqNo = decoder.asInt(7);
			EndSeqNo = decoder.asInt(16);
		} catch (Exception ex) {
			throw new SessionRejectMessageException(
					decoder.getSeqNum(),
					35,
					decoder.getMsgType(),
					SESSION_REJECT_REASON.VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG,
					String.format("Cannot retrieve BeginSeqNo(7) EndSeqNo(16).Error:",ex.getMessage()));
		}

		if ( BeginSeqNo<=0 || EndSeqNo<0 || (EndSeqNo<=BeginSeqNo && EndSeqNo!=0) || EndSeqNo>2147483647 ){
			throw new SessionRejectMessageException(
					decoder.getSeqNum(),
					35,
					decoder.getMsgType(),
					SESSION_REJECT_REASON.VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG,
					String.format("Invalid BeginSeqNo:%d and/or EndSeqNo:%d values",BeginSeqNo,EndSeqNo));
		}

		LOGGER_SESSION.info("Processing Resend Request BeginSeqNo:{} to EndSeqNo:{}",BeginSeqNo,EndSeqNo);
		
		List<FixMessageEncoder> msgList = persistence.getAllOutgoingMessagesInRange(BeginSeqNo, EndSeqNo);
		if ( msgList==null || msgList.isEmpty() ) {
			throw new SessionRejectMessageException(
					decoder.getSeqNum(),
					35,
					decoder.getMsgType(),
					SESSION_REJECT_REASON.VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG,
					String.format("No messages found in seq interval:%d..%d",decoder.getMsgType()));
		}

		for ( FixMessageEncoder msg : msgList ) {
			if ( fixMessageValidator.isAdministrativeMessage( msg.getMsgType()) || (retransmissionInterceptor!=null && !retransmissionInterceptor.canRetransmit(msg)) ) {
				LOGGER_SESSION.info("Skipping msg SeqNum:{}", msg.getSeqNum());
				sendAdminMessage( buildGapFillMessage(BeginSeqNo, msg.getSeqNum() ), BeginSeqNo);
			} else {
				LOGGER_SESSION.info("Retransmitting SeqNum:{}", msg.getSeqNum());
				sendDupMessage( buildRetransmissionMessage( msg ));		
			}
		}
	}

	public AbstractFixSession withMessageListener( String msgType, IFixMessageListener listener ) {
		this.msgListeners.put(msgType, listener);
		return this;
	}

	public AbstractFixSession withAllMessagesListener( IFixMessageListener listener ) {
		this.msgListeners.put("*", listener);
		return this;
	}

	public AbstractFixSession withSessionListener( IFixSessionListener listener) {
		this.sessionListener = listener;
		return this;
	}
	 
	public AbstractFixSession withRetransmissionInterceptor( IRetransmissionInterceptor retransmissionInterceptor ) {
		this.retransmissionInterceptor = retransmissionInterceptor;
		return this;
	}

	public IFixSessionListener getSessionListener( ) {
		return this.sessionListener;
	}

	public FixMessageEncoder newEncoder( String msgType ) {
		return FixMessageEncoder.newEncoder( sessionConfig.getDictionary(), msgType );
	}

	public FixMessageDecoder newDecoder(  byte[] buffer, int from, int len, long rcvNanoTime ) {
		return FixMessageDecoder.newDecoder( sessionConfig.getDictionary(), buffer, from, len, rcvNanoTime );
	}
	
	private void sendDupMessage( FixMessageEncoder enc ) {
		try {
			byte[] outMsg = enc.set( 52, TimeUtils.getSendingTime() ).forceBuild();
			send( outMsg );
		} catch (Exception e) {
			LOGGER_SESSION.error("Exception Handled in onMessage().",e);
		}
	}
	
	private void sendAdminMessage( FixMessageEncoder enc, int seqNum ) {
		try {
			byte[] outMsg = fillHeaderFields(enc, seqNum).build();
			send( outMsg );
		} catch (Exception e) {
			LOGGER_SESSION.error("Exception Handled in sendAdminMessage().",e);
		}
	}
	
	// To be overridden to put some data just before CRC calculation and send operation.
	// Used for example in crypto exchanges like Binance to set RawData header field.
	abstract FixMessageEncoder beforeSend( FixMessageEncoder enc );
	
	public void sendMessage( FixMessageEncoder enc ) {	
		acquireLock( wOutLock );
		try {
			FixMessageEncoder toBeSent = beforeSend( fillHeaderFields(enc, -1) );
			if ( toBeSent!=null ) {
				byte[] outMsg = toBeSent.build();
				if ( send(outMsg) ) {
					persistence.storeOutgoingMessage(enc.getSeqNum(), enc);
				}
			} else {
				persistence.decrementOutgoingSeq();
			}
		} catch (Exception e) {
			LOGGER_SESSION.error("Exception Handled in sendMessage().",e);
		} finally {
			wOutLock.unlock();
		}
	}
	
	private boolean send( byte[] outMsg ) {
		try {
			if ( transport.send( outMsg ) ) {
				LOGGER_MSG.info("SND: {}", new String(outMsg) );
				return true;
			}
			LOGGER_MSG.error("SND FAILED: {}", new String(outMsg) );
			return false;
		} catch (Exception e) {
			LOGGER_SESSION.error("Exception Handled in send().",e);
			return false;
		}
	}
	
	private final FixMessageEncoder fillHeaderFields( FixMessageEncoder encoder, int seqNum  ) throws InterruptedException {
		acquireLock( rOutLock );
		try {
			return encoder
					.set( 49, sessionConfig.getSenderCompId() )
					.set( 56, sessionConfig.getTargetCompId() )
					.set( 34, (seqNum>0)?seqNum:persistence.getAndIncrementOutgoingSeq() )
					.setIfAbsent( 52, TimeUtils.getSendingTime() );
		} finally {
			rOutLock.unlock();
		}
	}

	synchronized void resetIncomingSequence(int newSeqNo) {
		int currSeq = persistence.getLastIncomingSeq();
		if ( newSeqNo<currSeq ) {
			throw new RuntimeException(String.format("Invalid sequence reset: newSeqNo:%d < currSeq:%d",newSeqNo, currSeq) );
		} else if ( newSeqNo>currSeq ) { 
			persistence.setLastIncomingSeq(newSeqNo);
		}		
	}	

	void resetOutgoingSequence() {
		acquireLock( rOutLock );
		try {
			persistence.resetOutgoingSequence();
		} finally {
			rOutLock.unlock();
		}
	}
	
	private FixMessageEncoder buildGapFillMessage( int BeginSeqNo, int EndSeqNo ) {
		return fixMessageMapper.buildGapFillMessage(
				newEncoder( "2" ),
				BeginSeqNo,
				EndSeqNo);
	}

	private FixMessageEncoder buildHeartbeatMessage( String TestReqID ) {
		return fixMessageMapper.buildHeartbeatMessage(newEncoder( "0" ),TestReqID);
	}

	private FixMessageEncoder buildRejectMessage( SessionRejectMessageException ex ) {
		return fixMessageMapper.buildRejectMessage(newEncoder( "3" ),ex);
	}

	private FixMessageEncoder buildLogoutMessage( SessionRejectMessageException ex ) {
		return fixMessageMapper.buildLogoutMessage(newEncoder( "5" ),ex);
	}
	
	private FixMessageEncoder buildBusinessRejectMessage( BusinessRejectMessageException ex ) {
		return fixMessageMapper.buildBusinessRejectMessage(newEncoder( "j" ),ex);
	}

	private FixMessageEncoder buildRetransmissionMessage( FixMessageEncoder enc ) {
		return enc
			.set(43, true) // PossDupFlag 
			.set(122, (String)enc.get(52)) // OrigSendingTime = SendingTime 
		;
	}
	
	ITransport getTransport() {
		return transport;
	}

	public FixMessageMapper getFixMessageMapper() {
		return fixMessageMapper;
	}
	
	public void disconnect() {
		try {
			transport.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ClientFixSessionConfig getSessionConfig() {
		return sessionConfig;
	}
}
