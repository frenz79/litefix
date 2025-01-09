package com.litefix.models.session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.ByteUtils;
import com.litefix.commons.utils.MathUtils;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.transport.ITransport;
import com.litefix.modules.transport.ITransportListener;

public abstract class AbstractFixSession implements ITransportListener{

	public static final DateTimeFormatter UTC_TIMESTAMP_SEC = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss");
	public static final DateTimeFormatter UTC_TIMESTAMP_MILLIS = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

	final SessionStateMachine sessionSM;

	final ClientFixSessionConfig sessionConfig;

	public static long SENDING_TIME_ACCURACY_THREASHOLD_MILLIS = 1000L;

	public static final int CRC_BODY_FIELD_SIZE = 7;

	private Map<String,IFixMessageListener> msgListeners = new HashMap<>();
	private IFixSessionListener sessionListener;

	private final IPersistence<FixMessageEncoder> persistence;
	private final ITransport transport;
	private final FixMessageMapper fixMessageMapper;
	
	public AbstractFixSession(ITransport transport, IPersistence<FixMessageEncoder> persistence, ClientFixSessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
		this.transport = transport;
		this.persistence = persistence;
		this.sessionSM = new SessionStateMachine();
		this.fixMessageMapper = new FixMessageMapper();
	}

	public IFixMessageListener getMessageListener( String msgType ) {
		IFixMessageListener l = msgListeners.get(msgType);
		if ( l==null ) {
			l = msgListeners.get("*");
		}
		return l;
	}

	private long lastGapFillMessageWaitTime = 10_000l;
	private long lastGapFillMessageSendTime = 0l;
	
	@Override
	public void onMessage( byte[] buffer, int from, int len, long rcvNanoTime ) {
		try {
			if ( buffer.length-from<7 || !hasFixSignature( buffer, from ) ) {
				// TODO: handle garbage!
				System.out.println("Garbage detected...no fix signature found");
				return;
			}
			
			System.out.println("<< "+new String(buffer, from, len) );
	
			FixMessageDecoder decoder = newDecoder( buffer, from, len, rcvNanoTime );
			validate( sessionConfig, decoder, decoder.getMsgType() );
						
			if ( !sessionSM.isLoggedOn() ) {			
				switch(decoder.getMsgType()) {
				case "A": 
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
			ex1.printStackTrace();			
			sendMessage( buildBusinessRejectMessage(ex1));
		} catch( SessionRejectMessageException ex2 ) {
			// TODO: Do logout
			ex2.printStackTrace();			
			sendMessage( buildRejectMessage(ex2) );
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	private void handleApplicativeMessage( FixMessageDecoder decoder ) throws SessionRejectMessageException, BusinessRejectMessageException {
		
		if ( "4".equals(decoder.getMsgType())) {
			Integer NewSeqNo = decoder.asInt(36);
			Boolean GapFillFlag = decoder.asBoolean(123);
			if ( Boolean.TRUE.equals(GapFillFlag) ) {
				// Gap fill
				System.out.println("got GapFill to:"+NewSeqNo);
				resetIncomingSequence( NewSeqNo );
			} else {
				// Reset mode
				System.out.println("got SequenceReset to:"+NewSeqNo);
				resetIncomingSequence( NewSeqNo );					
			}				
			return;
		}
		
		int lastRcvMessageSeq = persistence.getLastIncomingSeq();
		
		// Sequence numbers check
		if ( lastRcvMessageSeq>=0 ) {
			int expectedIncomingSeqNum = lastRcvMessageSeq+1;
			if ( expectedIncomingSeqNum>decoder.getSeqNum() ) {
				System.out.println("Old or duplicated message detected, discarding message:"+decoder.getSeqNum()+". Expecting:"+expectedIncomingSeqNum+" but received:"+decoder.getSeqNum());
				return;
			} else if ( expectedIncomingSeqNum>decoder.getSeqNum() ) {
				System.out.println("Gap detected, discarding message:"+decoder.getSeqNum()+". Expecting:"+expectedIncomingSeqNum+" but received:"+decoder.getSeqNum());

				long now = System.currentTimeMillis();
				if (now-lastGapFillMessageSendTime>lastGapFillMessageWaitTime) {
					System.out.println("sending ResendRequest FROM: "+lastRcvMessageSeq+" TO: 0" );
					sendMessage( buildGapFillMessage( expectedIncomingSeqNum, 0 ));
					lastGapFillMessageSendTime = now;
				} else {
					System.out.println("Gap detected. Already sent ResendRequest FROM: "+expectedIncomingSeqNum+" TO: "+decoder.getSeqNum()+". Not sending another.");
				}
				return;
			}
		}	
		
		
		persistence.incLastIncomingSeq();
		
		switch(decoder.getMsgType()) {
		case "0": handleHeartbeat(decoder); break;
		case "1": handleTestRequest(decoder); break;
		case "2": handleGapFillRequest(decoder); break;
		default:
			IFixMessageListener listener = getMessageListener(decoder.getMsgType());
			if ( listener!=null ) {
				listener.onMessage(decoder);
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

		System.out.println("Processing Resend Request BeginSeqNo:"+BeginSeqNo+" to EndSeqNo:"+EndSeqNo);
		
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
			if ( isAdministrativeMessage( msg.getMsgType()) ) {
				System.out.println("Skipping Administrative msg SeqNum:"+msg.getSeqNum());
				sendAdminMessage( buildGapFillMessage(BeginSeqNo, msg.getSeqNum() ), BeginSeqNo);
			} else {
				System.out.println("Retransmitting SeqNum:"+msg.getSeqNum());
				sendDupMessage( buildRetransmissionMessage( msg ));
			}
		}
	}

	boolean isAdministrativeMessage(String msgType) {		
		return msgType.equals("0") 
				|| msgType.equals("A")
				|| msgType.equals("1") 
				|| msgType.equals("2")
				|| msgType.equals("3")
				|| msgType.equals("4")
				|| msgType.equals("j")
				;
	}

	public void addMessageListener( String msgType, IFixMessageListener listener ) {
		this.msgListeners.put(msgType, listener);
	}

	public void addAllMessagesListener( IFixMessageListener listener ) {
		this.msgListeners.put("*", listener);
	}

	public void addSessionListener( IFixSessionListener listener) {
		this.sessionListener = listener;
	}

	public IFixSessionListener getSessionListener( ) {
		return this.sessionListener;
	}

	public FixMessageEncoder newEncoder( String msgType ) {
		return FixMessageEncoder.newEncoder( sessionConfig.dictionary, msgType );
	}

	public FixMessageDecoder newDecoder(  byte[] buffer, int from, int len, long rcvNanoTime ) {
		return FixMessageDecoder.newDecoder( sessionConfig.dictionary, buffer, from, len, rcvNanoTime );
	}	

	public static String getSendingTime() {
		return LocalDateTime.now(ZoneOffset.UTC).format( UTC_TIMESTAMP_MILLIS );
	}

	// YYYYMMDD-HH:MM:SS.sss
	LocalDateTime toUTCTimestamp( String val ) {
		DateTimeFormatter formatter = (val.charAt(val.length()-4)=='.')?UTC_TIMESTAMP_MILLIS:UTC_TIMESTAMP_SEC;
		return LocalDateTime.parse(val, formatter);
	}	

	private boolean hasFixSignature( byte[] buffer, int pos ) {
		return (buffer[pos]=='8' 
				&& buffer[pos+1]=='=' 
				&& buffer[pos+2]=='F' 
				&& buffer[pos+3]=='I' 
				&& buffer[pos+4]=='X' 
				&& buffer[pos+5]=='.' 
				);
	}

	public boolean validate( ClientFixSessionConfig sessionConfig, FixMessageDecoder decoder, String msgType ) throws SessionRejectMessageException{
		int incomingMsgSeqNum = decoder.getSeqNum();
		int buffOffset = decoder.getMsgBuffFrom()+decoder.getMsgBuffLen();

		byte[] buffer = decoder.getMsgBuff();

		int checksum = MathUtils.calcChecksum(
				buffer, 
				decoder.getMsgBuffFrom(),
				buffOffset-CRC_BODY_FIELD_SIZE
				);

		if ( checksum!=ByteUtils.charsToInt(buffer[buffOffset-4], buffer[buffOffset-3],	buffer[buffOffset-2])){
			throw new SessionRejectMessageException(
					incomingMsgSeqNum,
					10,
					msgType,
					SESSION_REJECT_REASON.OTHER,
					"Bad CRC");	
		}

		if ( !decoder.isEqual(56, sessionConfig.getSenderCompIdBytes())) {
			throw new SessionRejectMessageException(
					incomingMsgSeqNum,
					56,
					msgType,
					SESSION_REJECT_REASON.COMPID_PROBLEM,
					"Invalid TargetCompID");
		}

		if ( !decoder.isEqual(49, sessionConfig.getTargetCompIdBytes())) {
			//if ( !sessionConfig.targetCompId.equals(incomingSenderCompId)) {
			throw new SessionRejectMessageException(
					incomingMsgSeqNum,
					49,
					msgType,
					SESSION_REJECT_REASON.COMPID_PROBLEM,
					"Invalid SenderCompID");
		}
		/*
	    if ((validationFlags & SENDINGTIME_ACCURACY) == SENDINGTIME_ACCURACY) {
	    	FixField sendingTimeField = msg.getHederField( IFixConst.StandardHeader.SendingTime );
	    	if (sendingTimeField==null) {
		    	throw new SessionRejectMessageException(
		    			msgSeqNum,
						52,
						msgType.getValue(),
						SESSION_REJECT_REASON.REQUIRED_TAG_MISSING,
						"SendingTime missing");
	    	}

	    	LocalDateTime sendingTime = decoder.asTimestamp( 52 );
	    	LocalDateTime sendingTime = TimeUtils.fromSendingTime(sendingTimeField.valueAsString());	    	
			if ( sendingTime.plus(Duration.ofMillis(SENDING_TIME_ACCURACY_THREASHOLD_MILLIS)).isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
	    		throw new SessionRejectMessageException(
		    			msgSeqNum,
						52,
						msgType.getValue(),
						SESSION_REJECT_REASON.SENDINGTIME_52_ACCURACY_PROBLEM,
						"SendingTime accuracy problem");
	    	}	    	
	    }
		 */
		return true;
	}

	private void sendAdminMessage( FixMessageEncoder enc, int seqNum ) {
		try {
			byte[] outMsg = fillHeaderFields(enc, seqNum).build();
			if ( transport.send( outMsg ) ) {
				System.out.println(">> "+new String(outMsg) );
			} else {
				System.out.println(">> WRITE FAILED");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendDupMessage( FixMessageEncoder enc ) {
		try {
			byte[] outMsg = enc.set( 52, getSendingTime() ).forceBuild();
			if ( transport.send( outMsg ) ) {
				System.out.println(">> "+new String(outMsg) );
			} else {
				System.out.println(">> WRITE FAILED");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage( FixMessageEncoder enc ) {
		try {
			byte[] outMsg = fillHeaderFields(enc, -1).build();
			if ( transport.send( outMsg ) ) {			
				// Trace outgoing messages
				persistence.storeOutgoingMessage(enc.getSeqNum(), enc);
				System.out.println(">> "+new String(outMsg) );
			} else {
				System.out.println(">> WRITE FAILED");
			}
		} catch (IOException e) {
			e.printStackTrace();
			purgeOutgoingMessage( enc );
		}
	}
	
	private final FixMessageEncoder fillHeaderFields( FixMessageEncoder encoder, int seqNum  ) {	
		return encoder
				.set( 49, sessionConfig.getSenderCompId() )
				.set( 56, sessionConfig.getTargetCompId() )
				.set( 34, (seqNum>0)?seqNum:persistence.getAndIncrementOutgoingSeq() )
				.set( 52, getSendingTime() );
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
		persistence.resetOutgoingSequence();
	}
	
	private void purgeOutgoingMessage(FixMessageEncoder enc) {
		// TODO Auto-generated method stub
	}
	
	private FixMessageEncoder buildGapFillMessage( int BeginSeqNo, int EndSeqNo ) {
		return fixMessageMapper.buildGapFillMessage(
				newEncoder( "2" ),
				BeginSeqNo,
				EndSeqNo);
	}

	private FixMessageEncoder buildHeartbeatMessage( String TestReqID ) {
		return fixMessageMapper.buildHeartbeatMessage(
				newEncoder( "0" ),
				TestReqID);
	}

	private FixMessageEncoder buildRejectMessage( SessionRejectMessageException ex ) {
		return fixMessageMapper.buildRejectMessage(
				newEncoder( "3" ),
				ex);
	}

	private FixMessageEncoder buildBusinessRejectMessage( BusinessRejectMessageException ex ) {
		return fixMessageMapper.buildBusinessRejectMessage(
				newEncoder( "j" ),
				ex);
	}

	private FixMessageEncoder buildRetransmissionMessage( FixMessageEncoder enc ) {
		return enc
			.set(43, true) // PossDupFlag 
			.set(122, (String)enc.get(52)) // OrigSendingTime = SendingTime 
		;
	}
	
	public ITransport getTransport() {
		return transport;
	}

	public FixMessageMapper getFixMessageMapper() {
		return fixMessageMapper;
	}
}
