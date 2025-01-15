package com.litefix.models.session;

import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.ArrayUtils;
import com.litefix.commons.utils.ByteUtils;
import com.litefix.commons.utils.MathUtils;
import com.litefix.models.fixmessage.FixMessageDecoder;

public class FixMessageValidator implements IFixMessageValidator {

	private static long SENDING_TIME_ACCURACY_THREASHOLD_MILLIS = 1000L;
	
	private static final int CRC_BODY_FIELD_SIZE = 7;
	private final byte[] beginStringBytes;
	private final byte   fieldSeparator;
	private final ClientFixSessionConfig sessionConfig;
	
	public FixMessageValidator( ClientFixSessionConfig sessionConfig ) {
		this.beginStringBytes = sessionConfig.getDictionary().getBeginString().getBytes();
		this.fieldSeparator = (byte)sessionConfig.getDictionary().getFieldSep();
		this.sessionConfig = sessionConfig;
	}
	
	/*
	 * A message shall be considered garbled if any of the following occur as a
	 * result of encoding, decoding, or transmission errors:
	 * 
	 * - BeginString(8) is not the first tag in a message or is not one of the defined
	 *   FIX session profile identifiers (“8=FIX.4.4”, “8=FIXT.1.1”). 
	 * - BodyLength(9) is not the second tag in a message or does not contain the correct byte count.
	 * - MsgType(35) is not the third tag in a message. 
	 * - Checksum(10) is not the last tag or contains an incorrect value.
	 */		
	@Override
	public boolean isGarbled( byte[] buffer, int from, int len ) {		
		if ( buffer.length-from<beginStringBytes.length+2 
			|| buffer[from]!='8'
			|| buffer[from+1]!='='
			|| ArrayUtils.findArray(buffer, beginStringBytes, from+2, from+2+beginStringBytes.length)<0 ) {
			// TODO: handle garbage!
			System.out.println("Garbage detected...no fix signature found");
			return true;
		}
		
		int offset = from + beginStringBytes.length + 2;
		
		if ( buffer[offset]!=fieldSeparator || buffer[++offset]!='9' || buffer[++offset]!='=' || buffer[++offset]==fieldSeparator ) {
			System.out.println("Garbage detected...tag 9 not found in second position");
			return true;
		}
		
		int i=0;
		for (; i<16; i++) {
			if (buffer[offset+i]==fieldSeparator) {
				break;
			}
		}
		if (i==16) {
			System.out.println("Garbage detected...tag 9 has invalid value");
			return true;
		}
		offset += i;
		
		if ( buffer[++offset]!='3' || buffer[++offset]!='5' || buffer[++offset]!='=' || buffer[++offset]==fieldSeparator ) {
			System.out.println("Garbage detected...tag 35 not found in third position");
			return true;
		}
		
		if ( buffer[from+len-7]!='1' || buffer[from+len-6]!='0' || buffer[from+len-5]!='=' || buffer[from+len-5]==fieldSeparator ) {
			System.out.println("Garbage detected...tag 10 not found in last position");
			return true;
		}
		
		int buffOffset = from + len;
		int checksum = MathUtils.calcChecksum(buffer, from, buffOffset-CRC_BODY_FIELD_SIZE );

		if ( checksum!=ByteUtils.charsToInt(buffer[buffOffset-4], buffer[buffOffset-3],	buffer[buffOffset-2])){
			System.out.println("Garbage detected...Bad CRC");
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isAdministrativeMessage(String msgType) {		
		return msgType.equals("0") 
				|| msgType.equals("A")
				|| msgType.equals("1") 
				|| msgType.equals("2")
				|| msgType.equals("3")
				|| msgType.equals("4")
				|| msgType.equals("j")
				;
	}
	
	@Override
	public boolean isValidMessage( FixMessageDecoder decoder, String msgType ) throws SessionRejectMessageException{
		int incomingMsgSeqNum = decoder.getSeqNum();
		
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
}
