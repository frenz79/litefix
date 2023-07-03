package com.litefix.modules.impl;

import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;

public class FixMessageValidator {

	public static final int CRC       =  1;  // 0001
	public static final int REVERSE   =  2;  // 0010
	public static final int FULL_STOP =  4;  // 0100
	public static final int EMPHASISE =  8;  // 1000
		
	public static final int ALL_OPTS  = 15; // 1111
	
	private final int validationFlags;
	private String senderCompId;
	private String targetCompId;
	
	private byte[] senderCompIdBytes;
	private byte[] targetCompIdBytes;
	
	public FixMessageValidator( int flags ){
		this.validationFlags = flags;
	}
	
	public boolean isValid( FixMessage msg ){
	    if ((validationFlags & CRC) == CRC) {
	    	
	    }
	    
	    if ( !msg.is( FixTag.HEADER_TAG_SENDER_COMP_ID, targetCompIdBytes )){
	    	System.out.println("Invalid TargetCompID");
	    	return false;
	    }
	    if ( !msg.is( FixTag.HEADER_TAG_TARGET_COMP_ID, senderCompIdBytes )){
	    	System.out.println("Invalid SenderCompID");
	    	return false;
	    }
	    
/*
		FixField holder = new FixField();

		FixField senderCompIDField = msg.getField( FixTag.HEADER_TAG_SENDER_COMP_ID, holder );
		if ( senderCompIDField==null ||  !senderCompIDField.is( targetCompIdBytes ) ) {
			return false;
		}
		
		FixField targetCompIDField = msg.getField( FixTag.HEADER_TAG_TARGET_COMP_ID, holder );
		if ( targetCompIDField==null ||  !targetCompIDField.is( senderCompIdBytes ) ) {
			return false;
		}
*/
		return true;
	}

	public String getSenderCompId() {
		return senderCompId;
	}

	public void setSenderCompId(String senderCompId) {
		this.senderCompId = senderCompId;
		this.senderCompIdBytes = senderCompId.getBytes();
	}

	public String getTargetCompId() {
		return targetCompId;
	}

	public void setTargetCompId(String targetCompId) {
		this.targetCompId = targetCompId;
		this.targetCompIdBytes = targetCompId.getBytes();
	}
	
}
