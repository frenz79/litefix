package com.litefix.commons.exceptions;

public class SessionRejectMessageException extends Exception {

	public SessionRejectMessageException(int refSeqNum, int refTagID, String refMsgType, 
			SESSION_REJECT_REASON sessionRejectReason, String text, int encodedTextLen, String encodedText) {
		super();
		RefSeqNum = refSeqNum;
		RefTagID = refTagID;
		RefMsgType = refMsgType;
		SessionRejectReason = sessionRejectReason;
		Text = text;
		EncodedTextLen = encodedTextLen;
		EncodedText = encodedText;
	}
	
	public SessionRejectMessageException(int refSeqNum, int refTagID, String refMsgType, 
			SESSION_REJECT_REASON sessionRejectReason, String text ) {
		this(refSeqNum, refTagID, refMsgType, sessionRejectReason, text, 0, null);
	}

	private final int RefSeqNum;
	private final int RefTagID;
	private final String RefMsgType;
	private final SESSION_REJECT_REASON SessionRejectReason;
	private final String Text;
	private final int EncodedTextLen;
	private final String EncodedText;
	
	public static enum SESSION_REJECT_REASON {
		  INVALID_TAG_NUMBER(0)
		, REQUIRED_TAG_MISSING(1)
		, TAG_NOT_DEFINED_FOR_THIS_MESSAGE_TYPE(2)
		, UNDEFINED_TAG(3)
		, TAG_SPECIFIED_WITHOUT_A_VALUE(4)
		, VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG(5)
		, INCORRECT_DATA_FORMAT_FOR_VALUE(6)
		, DECRYPTION_PROBLEM(7)
		, SIGNATURE_89_PROBLEM(8)
		, COMPID_PROBLEM(9)
		, SENDINGTIME_52_ACCURACY_PROBLEM(10)
		, INVALID_MSGTYPE_35(11)
		, XML_VALIDATION_ERROR(12)
		, TAG_APPEARS_MORE_THAN_ONCE(13)
		, TAG_SPECIFIED_OUT_OF_REQUIRED_ORDER(14)
		, REPEATING_GROUP_FIELDS_OUT_OF_ORDER(15)
		, INCORRECT_NUMINGROUP_COUNT_FOR_REPEATING_GROUP(16)
		, NON_DATA_VALUE_INCLUDES_FIELD_DELIMITER_SOH_CHARACTER(17)
		, OTHER(99);
		
		private final int value;
		
		SESSION_REJECT_REASON(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}

	public int getRefSeqNum() {
		return RefSeqNum;
	}

	public int getRefTagID() {
		return RefTagID;
	}

	public String getRefMsgType() {
		return RefMsgType;
	}

	public SESSION_REJECT_REASON getSessionRejectReason() {
		return SessionRejectReason;
	}

	public String getText() {
		return Text;
	}

	public int getEncodedTextLen() {
		return EncodedTextLen;
	}

	public String getEncodedText() {
		return EncodedText;
	}
}
