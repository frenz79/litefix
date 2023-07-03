package com.litefix;

import com.litefix.commons.exceptions.InvalidSessionException;
import com.litefix.modules.IFixMessagePool;
import com.litefix.modules.IPersistence;
import com.litefix.modules.ITransport;
import com.litefix.modules.impl.AsyncMessagesDispatcher;
import com.litefix.modules.impl.FixMessageValidator;

public class FixSessionBuilder {

	private FixSession session;
	
	public FixSessionBuilder( IFixSessionListener fixSessionListener ) {
		this.session = new ClientFixSession( fixSessionListener );
	}
		
	public FixSessionBuilder withMessagesDispatcher(AsyncMessagesDispatcher messagesDispatcher) {
		this.session.messagesDispatcher = messagesDispatcher;
		return this;
	}
	
	public FixSessionBuilder withMessagesValidator(FixMessageValidator messageValidator) {
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
	
	public FixSessionBuilder withPersistence( IPersistence persistence ) {
		this.session.persistence = persistence;
		return this;
	}
	
	public FixSession build() throws InvalidSessionException {
		this.session.validate();
		this.session.doWarmup(5000);
		return this.session;
	}
}
