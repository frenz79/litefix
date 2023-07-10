package com.litefix;

import com.litefix.commons.exceptions.InvalidSessionException;
import com.litefix.models.FixMessage;
import com.litefix.modules.IFixMessagePool;
import com.litefix.modules.IFixMessageValidator;
import com.litefix.modules.IMessagesDispatcher;
import com.litefix.modules.IPersistence;
import com.litefix.modules.ITransport;

public class FixSessionBuilder {

	private FixSession session;
	
	public FixSessionBuilder( String host, int port, IFixSessionListener fixSessionListener ) {
		this.session = new ClientFixSession( host, port, fixSessionListener);
	}
	
	public FixSession build() throws InvalidSessionException {
		this.session.validate();
		this.session.doWarmup(5000);
		return this.session;
	}
		
	public FixSessionBuilder withMessagesDispatcher(IMessagesDispatcher messagesDispatcher) {
		this.session.messagesDispatcher = messagesDispatcher;
		return this;
	}
	
	public FixSessionBuilder withMessagesValidator(IFixMessageValidator messageValidator) {
		this.session.messageValidator = messageValidator;
		return this;
	}
	
	public FixSessionBuilder withBeginString( String beginString ) {
		this.session.beginString = beginString;
		return this;
	}

	public FixSessionBuilder withSenderCompId( String senderCompId ) {
		this.session.senderCompId = senderCompId;
		return this;
	}
	
	public FixSessionBuilder withResetSeqOnLogon( boolean resetSeqOnLogon ) {
		this.session.resetSeqOnLogon = resetSeqOnLogon;
		return this;
	}
	
	public FixSessionBuilder withResetSeqOnDisconnect( boolean resetSeqOnDisconnect ) {
		this.session.resetSeqOnDisconnect = resetSeqOnDisconnect;
		return this;
	}
	
	public FixSessionBuilder withHbIntervalSec( int hbIntervalSec ) {
		this.session.hbIntervalSec = hbIntervalSec;
		return this;
	}
	
	public FixSessionBuilder withTargetCompId( String targetCompId ) {
		this.session.targetCompId = targetCompId;
		return this;
	}
	
	public FixSessionBuilder withMessagePool( IFixMessagePool fixMessageFactory ) {
		this.session.messagePool = fixMessageFactory;
		return this;
	}
	
	public FixSessionBuilder withTransport( ITransport transport ) {
		this.session.transport = transport;
		return this;
	}
	
	public FixSessionBuilder withPersistence( IPersistence<FixMessage> persistence ) {
		this.session.persistence = persistence;
		return this;
	}
	
	public FixSessionBuilder withLogonTimeoutSec(int logonTimeoutSec) {
		this.session.logonTimeoutSec = logonTimeoutSec;
		return this;
	}

	public FixSessionBuilder withAutomaticReconnect(boolean automaticReconnect, long automaticReconnectRetryDelayMillis ) {
		this.session.automaticReconnect = automaticReconnect;
		this.session.automaticReconnectRetryDelayMillis = automaticReconnectRetryDelayMillis;
		return this;
	}

	public FixSessionBuilder withAutomaticLogonOnConnect(boolean automaticLogonOnConnect) {
		this.session.automaticLogonOnConnect = automaticLogonOnConnect;
		return this;
	}
	
	public FixSessionBuilder withAutomaticLogonOnLogout(boolean automaticLogonOnLogout) {
		this.session.automaticLogonOnLogout = automaticLogonOnLogout;
		return this;
	}	
	
}
