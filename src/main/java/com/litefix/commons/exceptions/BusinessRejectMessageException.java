package com.litefix.commons.exceptions;

public class BusinessRejectMessageException extends Exception {

	private final int RefSeqNum;
	private final String RefMsgType;
	private final String BusinessRejectRefID;
	private final BUSINESS_REJECT_REASON BusinessRejectReason;
	private final String Text;
	
	public static enum BUSINESS_REJECT_REASON {
		  OTHER(0)
		, UNKNOWN_ID(1)
		, UNKNOWN_SECURITY(2)
		, UNSUPPORTED_MESSAGE_TYPE(3)
		, APPLICATION_NOT_AVAILABLE(4)
		, CONDITIONALLY_REQUIRED_FIELD_MISSING(5);

		private final int value;
		
		BUSINESS_REJECT_REASON(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	public BusinessRejectMessageException(int refSeqNum, String refMsgType, String businessRejectRefID,
			BUSINESS_REJECT_REASON businessRejectReason, String text) {
		super();
		RefSeqNum = refSeqNum;
		RefMsgType = refMsgType;
		BusinessRejectRefID = businessRejectRefID;
		BusinessRejectReason = businessRejectReason;
		Text = text;
	}

	public int getRefSeqNum() {
		return RefSeqNum;
	}

	public String getRefMsgType() {
		return RefMsgType;
	}

	public String getBusinessRejectRefID() {
		return BusinessRejectRefID;
	}

	public BUSINESS_REJECT_REASON getBusinessRejectReason() {
		return BusinessRejectReason;
	}

	public String getText() {
		return Text;
	}

}
