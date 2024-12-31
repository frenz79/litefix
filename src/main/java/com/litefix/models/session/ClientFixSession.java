package com.litefix.models.session;

import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.transport.IClientTransport;

public class ClientFixSession extends AbstractFixSession {
	
	public ClientFixSession(IClientTransport transport, IPersistence<FixMessageEncoder> persistence, ClientFixSessionConfig sessionConfig ) {
		super(transport, persistence, sessionConfig);
	}

	public AbstractFixSession doConnect( ) throws Exception {
		((IClientTransport)getTransport( )).connect( sessionConfig.getServerHost(), sessionConfig.getServerPort(), this );
		return this;
	}
	
	public ClientFixSession doLogon( FixMessageEncoder encoder ){	
		if ( encoder==null ) {
			encoder = newEncoder("A");
		}
		getFixMessageMapper().buildLogonMessage( encoder, sessionConfig );
		sendMessage( encoder );
		return this;
	}
	
	public ClientFixSession doLogon( ){		
		return doLogon( null );
	}
	
	// IClientTransportListener
	@Override
	public void onConnect(boolean status) {
		sessionSM.connected(status);
		if ( sessionConfig.getResetSeqNumFlag() == 'Y' ) {
			resetOutgoingSequence();
		}
		getSessionListener().onConnect(status);
	}

	// IClientTransportListener
	@Override
	public void onError(Throwable cause) {
		sessionSM.connected(false);
		if ( sessionConfig.getResetSeqNumFlag() == 'Y' ) {
			resetOutgoingSequence();
		}
		cause.printStackTrace();
	}
}
