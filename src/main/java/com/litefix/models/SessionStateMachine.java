package com.litefix.models;

public class SessionStateMachine {

	private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
	private LogonStatus logonStatus = LogonStatus.LOGGED_OUT;
	
	private long lastMessageReceivedNanos = 0L;
	private long lastMessageSentNanos = 0L;
	private long lastTestRequestSentNanos = 0L;
	
	private long logonSentNanos = 0L;
	private long logonRespNanos = 0L;		
	
	private boolean resending = false;
	
	public enum ConnectionStatus {
		DISCONNECTED,
		CONNECTED
	}
	
	public enum LogonStatus {		
		LOGON_SENT,
		LOGGED_IN,
		LOGGED_OUT,
		LOGON_TIMEOUT	
	}
	
	public boolean isConnected( ) {
		return ConnectionStatus.CONNECTED.equals(this.connectionStatus);
	}
	

	public void connected(boolean status) {
		this.connectionStatus = (status)?ConnectionStatus.CONNECTED:ConnectionStatus.DISCONNECTED;
		this.logonStatus = LogonStatus.LOGGED_OUT;
		this.logonRespNanos = 0L;
		this.logonSentNanos = 0L;
	}

	public void logonSent() {
		if (!isConnected()) {
			throw new RuntimeException("Invalid status logonSent when disconnected");
		}
		this.logonStatus = LogonStatus.LOGON_SENT;
		this.logonSentNanos = System.nanoTime();
	}
	
	public void logon( boolean result ) {
		this.logonRespNanos = System.nanoTime();
		if ( result ) {
			this.logonStatus = LogonStatus.LOGGED_IN;
		} else {
			this.logonStatus = LogonStatus.LOGGED_OUT;
		}
	}
	
	public void loggedOut() {
		this.logonStatus = LogonStatus.LOGGED_OUT;
		this.logonRespNanos = 0L;
		this.logonSentNanos = 0L;
	}

	public boolean isLoggedOn() {
		return LogonStatus.LOGGED_IN.equals(logonStatus);
	}
	
	public long getLastMessageReceivedNanos() {
		return lastMessageReceivedNanos;
	}

	public void setLastMessageReceivedNanos(long lastMessageReceivedNanos) {
		this.lastMessageReceivedNanos = lastMessageReceivedNanos;
	}

	public long getLastMessageSentNanos() {
		return lastMessageSentNanos;
	}

	public void setLastMessageSentNanos(long lastMessageSentNanos) {
		this.lastMessageSentNanos = lastMessageSentNanos;
	}

	public boolean isWaitingForLoginResp() {
		return LogonStatus.LOGON_SENT.equals(logonStatus);
	}

	public void setResending(boolean resending) {
		this.resending = resending;
	}

	public void setLastTestRequestSentNanos(long now) {
		this.lastTestRequestSentNanos = now;
	}

	public long getLastTestRequestSentNanos() {
		return lastTestRequestSentNanos;
	}

}
