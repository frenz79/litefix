package com.litefix.models.session;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageEncoder;

public class FixMessageMapper {
	
	public FixMessageEncoder buildLogonMessage( FixMessageEncoder encoder, ClientFixSessionConfig sessionConfig) {
		return encoder
			.set(108, sessionConfig.getHeartBtInt() )
			.set(141, sessionConfig.getResetSeqNumFlag() )
			.set( 98, sessionConfig.getEncryptMethod() );
	}
	
	public FixMessageEncoder buildHeartbeatMessage( FixMessageEncoder encoder, String TestReqID ) {
		return encoder
			.set(112, TestReqID);
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
}
