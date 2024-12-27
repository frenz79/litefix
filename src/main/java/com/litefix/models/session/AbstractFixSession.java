package com.litefix.models.session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.ByteUtils;
import com.litefix.commons.utils.MathUtils;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.modules.transport.ITransport;

public abstract class AbstractFixSession {

	private static final byte[] MSG_TYPE_SIGNATURE_BYTES = "35=".getBytes();
	
	public static final DateTimeFormatter UTC_TIMESTAMP_SEC = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss");
	public static final DateTimeFormatter UTC_TIMESTAMP_MILLIS = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

	final SessionStateMachine sessionSM;
	final AtomicInteger incomingSeqNum = new AtomicInteger(1);
	final AtomicInteger outgoingSeqNum = new AtomicInteger(0);
	final ClientFixSessionConfig sessionConfig;
	
	public static long SENDING_TIME_ACCURACY_THREASHOLD_MILLIS = 1000L;
		
	public static final int CRC_BODY_FIELD_SIZE = 7;
		
	private Map<String,IFixMessageListener> msgListeners = new HashMap<>();
	private IFixSessionListener sessionListener;

	private final ITransport transport;
	
	public AbstractFixSession(ITransport transport, ClientFixSessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
		this.transport = transport;
		this.sessionSM = new SessionStateMachine();
	}
	
	public IFixMessageListener getMessageListener( String msgType ) {
		IFixMessageListener l = msgListeners.get(msgType);
		if ( l==null ) {
			l = msgListeners.get("*");
		}
		return l;
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
	
	public FixMessageEncoder buildLogonMessage( FixMessageEncoder encoder ) {
		return encoder
			.set(108, sessionConfig.getHeartBtInt() )
			.set(141, sessionConfig.getResetSeqNumFlag() )
			.set( 98, sessionConfig.getEncryptMethod() );
	}
	
	public FixMessageEncoder buildHeartbeatMessage( FixMessageEncoder encoder ) {
		return encoder;
	}
	
	public FixMessageEncoder buildRejectMessage( FixMessageEncoder encoder, SessionRejectMessageException ex ) {
		return encoder
		 	.set(45, ex.getRefSeqNum())
		 	.set(371, ex.getRefTagID())
		 	.set(372, ex.getRefMsgType())
		 	.set(373, ex.getSessionRejectReason().ordinal())			 
		 	.set(58, ex.getText());
	}
	
	public FixMessageEncoder buildBusinessRejectMessage( FixMessageEncoder encoder, BusinessRejectMessageException ex ) {
		return encoder
			.set(45, ex.getRefSeqNum())
			.set(379, ex.getBusinessRejectRefID())
			.set(372, ex.getRefMsgType())
			.set(380, ex.getBusinessRejectReason().ordinal())			 
			.set(58, ex.getText());
	}
	
	public FixMessageEncoder buildGapFillMessage( FixMessageEncoder encoder, int BeginSeqNo, int EndSeqNo ) {
		return encoder
			.set(7, BeginSeqNo)
			.set(16, EndSeqNo);
	}
	
	private long nextOutgoingSequence() {
		return outgoingSeqNum.incrementAndGet();
	}
	
	void traceOutgoingMessage(FixMessageEncoder enc) {
		// TODO Auto-generated method stub
	}
	
	int traceIncomingMessage(FixMessageDecoder dec) {
		// TODO Auto-generated method stub
		
		return incomingSeqNum.incrementAndGet();
	}
	
	synchronized void resetIncomingSequence(int newSeqNo) {
		int currSeq = incomingSeqNum.get();
		if ( newSeqNo<currSeq ) {
			throw new RuntimeException("Invalid sequence reset");
		} else if ( newSeqNo>currSeq ) { 
			incomingSeqNum.set(newSeqNo);
		}		
	}	
	
	public static String getSendingTime() {
		return LocalDateTime.now(ZoneOffset.UTC).format( UTC_TIMESTAMP_MILLIS );
	}
	
	// YYYYMMDD-HH:MM:SS.sss
	LocalDateTime toUTCTimestamp( String val ) {
		DateTimeFormatter formatter = (val.charAt(val.length()-4)=='.')?UTC_TIMESTAMP_MILLIS:UTC_TIMESTAMP_SEC;
		return LocalDateTime.parse(val, formatter);
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
	
	public void sendMessage( FixMessageEncoder enc ) {
		try {			
			transport.send( fillHeaderFields(enc).build() );			
			// Trace outgoing messages
			traceOutgoingMessage( enc );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final FixMessageEncoder fillHeaderFields( FixMessageEncoder encoder  ) {	
		return encoder
				.set( 49, sessionConfig.getSenderCompId() )
				.set( 56, sessionConfig.getTargetCompId() )
				.set( 34, nextOutgoingSequence())
				.set( 52, getSendingTime() );
	}

	public ITransport getTransport() {
		return transport;
	}
	
	/*
	boolean processResendRequest( FixMessage msg ) {
		try {
			FixField beginSeqNo = msg.getField( IFixConst.ResendRequest.BeginSeqNo );
			FixField endSeqNo = msg.getField( IFixConst.ResendRequest.EndSeqNo );
			int maxEndSeqIdx = this.persistence.getLastOutgoingSeq();			
			int endIdx = (endSeqNo.is("0"))?maxEndSeqIdx:endSeqNo.valueAsInt();
			
			if ( endIdx>maxEndSeqIdx ) {
				this.msgSender.sendSessionReject( 
					msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), 
					String.format("Invalid Resend Request: BeginSeqNo (%d) is greater than expected (%d).",beginSeqNo, endSeqNo),
					msg.getMsgType().toString()
				);
				return false;
			}
			if ( endIdx>2147483647 ) {
				this.msgSender.sendSessionReject( 
					msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), 
					"nvalid Resend Request: BeginSeqNo <= 0.",
					msg.getMsgType().toString()
				);
				return false;
			}
			
			int startIdx = beginSeqNo.valueAsInt();
			
			for ( int i=startIdx; i<=endIdx; i++) {
				FixMessage dupMsg = this.persistence.findOutgoingMessageBySeq( i );
				if ( dupMsg!=null && !dupMsg.getMsgType().in("A", "5", "2", "0", "1", "4")) {
					this.session.send(dupMsg, true);
				}
			}
			return true;
		} catch ( Exception ex ) {
			ex.printStackTrace();
			try {
				this.msgSender.sendSessionReject( msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), ex.getMessage(), msg.getMsgType().toString() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	boolean processGapFillRequest( FixMessage msg ) {
		try {
			int lastIncomingSeq = this.persistence.getLastIncomingSeq();
			int seqNum = msg.getField(IFixConst.StandardHeader.MsgSeqNum).valueAsInt();
			int newSeqNo = msg.getField(IFixConst.ResendRequest.NewSeqNo).valueAsInt();
			
			if ( newSeqNo<this.persistence.getLastOutgoingSeq() ) {						
				this.msgSender.sendSessionReject( 
					msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), 
					String.format("NewSeqNo too small expected %d, received %d", lastIncomingSeq+1, newSeqNo), 
					"4"
				);
				return false;
			}
			
			int startIdx = seqNum;
			int endIdx = newSeqNo;
			
			for ( int i=startIdx; i<=endIdx; i++) {
				FixMessage dupMsg = this.persistence.findOutgoingMessageBySeq( i );
				if ( dupMsg!=null && !dupMsg.getMsgType().in("A", "5", "2", "0", "1", "4")) {
					this.session.send(dupMsg, true);
				}
			}
			
			return true;
		} catch( Exception ex ) {
			ex.printStackTrace( );
			try {
				this.msgSender.sendSessionReject( msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), ex.getMessage(), "4" );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	boolean processLogonResp( FixMessage msg ) {
		try {
			int lastIncomingSeq = this.persistence.getLastIncomingSeq();
			int seqNum = msg.getField(IFixConst.StandardHeader.MsgSeqNum).valueAsInt();	
			
			if ( !this.session.ignoreSeqNumTooLowAtLogon ) {						
				int delta = seqNum - lastIncomingSeq;
				if ( delta>1 ) {					
					this.msgSender.sendSessionReject( 
						msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), 
						String.format("Incoming seq too small in Logon(A) message, expected %d, received %d", lastIncomingSeq+1, seqNum), 
						"A"
					);
					return false;
				}
			}
			
			FixField NextExpectedMsgSeqNum = msg.getField(IFixConst.Logon.NextExpectedMsgSeqNum);
			if ( NextExpectedMsgSeqNum!=null ) {
				if ( seqNum!=lastIncomingSeq+1 ) {
					this.msgSender.sendGapFillRequest( seqNum, lastIncomingSeq+1 );
				}
			}
			
			return true;
		} catch( Exception ex ) {
			ex.printStackTrace( );
			try {
				this.msgSender.sendSessionReject( msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), ex.getMessage(), "A" );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	*/
}
