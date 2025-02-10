package com.litefix.models.md;

import com.litefix.models.md.enums.SessionRejectReason;

public class Reject extends AbstractMarketData {

	private long refSeqNum;
	private int refTagID;
	private String refMsgType;
	private SessionRejectReason sessionRejectReason;
	private int errorCode;
	private String text;
	
	public Reject() {
		super("");
	}

	public long getRefSeqNum() {
		return refSeqNum;
	}

	public void setRefSeqNum(long refSeqNum) {
		this.refSeqNum = refSeqNum;
	}

	public int getRefTagID() {
		return refTagID;
	}

	public void setRefTagID(int refTagID) {
		this.refTagID = refTagID;
	}

	public String getRefMsgType() {
		return refMsgType;
	}

	public void setRefMsgType(String refMsgType) {
		this.refMsgType = refMsgType;
	}

	public SessionRejectReason getSessionRejectReason() {
		return sessionRejectReason;
	}

	public void setSessionRejectReason(SessionRejectReason sessionRejectReason) {
		this.sessionRejectReason = sessionRejectReason;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
