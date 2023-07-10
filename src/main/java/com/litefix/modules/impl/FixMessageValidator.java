package com.litefix.modules.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.IFixConst;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.MathUtils;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType.TAG;
import com.litefix.modules.IFixMessageValidator;

public class FixMessageValidator implements IFixMessageValidator{

	public static long SENDING_TIME_ACCURACY_THREASHOLD_MILLIS = 1000L;
	
	public static final int CRC       =  1;  // 0001
	public static final int COMPID    =  2;  // 0010
	public static final int SENDINGTIME_ACCURACY =  4;  // 0100
	
	public static final int CRC_BODY_FIELD_SIZE = 7;
	
	private final int validationFlags;
	
	private final byte[] senderCompIdBytes;
	private final byte[] targetCompIdBytes;
	
	public FixMessageValidator( String senderCompId, String targetCompId, int flags ){
		this.validationFlags = flags;
		this.senderCompIdBytes = senderCompId.getBytes();
		this.targetCompIdBytes = targetCompId.getBytes();
	}
	
	@Override
	public boolean validate( FixMessage msg ) throws SessionRejectMessageException{
		TAG msgType = msg.getMsgType().getTag();
		int msgSeqNum = msg.getHederField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt();
		
	    if ((validationFlags & CRC) == CRC) {
	    	String calculatedCrc = NumbersCache.toPaddedString(
	    			MathUtils.calcChecksum(msg.getBuffer().array(), 0, msg.getBuffer().limit()-CRC_BODY_FIELD_SIZE));
	    	if ( !msg.is( IFixConst.StandardTrailer.CheckSum, calculatedCrc.getBytes() )){
		    	throw new SessionRejectMessageException(
		    			msgSeqNum,
		    			10,
						msgType.getValue(),
						SESSION_REJECT_REASON.OTHER,
						"Bad CRC");	
	    	}
	    }
	    
	    if ((validationFlags & COMPID) == COMPID) {	    
		    if ( !msg.is( IFixConst.StandardHeader.SenderCompID, targetCompIdBytes )){
		    	throw new SessionRejectMessageException(
		    			msgSeqNum,
						49,
						msgType.getValue(),
						SESSION_REJECT_REASON.COMPID_PROBLEM,
						"Invalid SenderCompID");
		    }
		    if ( !msg.is( IFixConst.StandardHeader.TargetCompID, senderCompIdBytes )){
		    	throw new SessionRejectMessageException(
		    			msgSeqNum,
						56,
						msgType.getValue(),
						SESSION_REJECT_REASON.COMPID_PROBLEM,
						"Invalid TargetCompID");
		    }
	    }

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
	    
		return true;
	}
	
}
