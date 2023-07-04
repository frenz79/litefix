package com.litefix;

import com.litefix.commons.IFixConst;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.SessionStatus;

public class ClientFixSession extends FixSession {

	public ClientFixSession(IFixSessionListener fixSessionListener) {
		super(fixSessionListener);
	}

	public ClientFixSession doConnect( String host, int port ) throws Exception {
		transport.connect( host, port );
		fixSessionListener.onConnection( true );
		startMainLoop();
		return this;
	}
	
	public ClientFixSession doLogon( ) throws Exception {
		return doLogon( new FixField[0] );
	}

	public ClientFixSession doLogon( FixField ... additionalFields ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("A")
				.addField( IFixConst.EncryptMethod, "0" ) // EncryptMethod
				.addField( IFixConst.HeartBtInt, hbIntervalSec )
				.addField( IFixConst.ResetSeqNumFlag, (resetSeqOnLogon)?'Y':'N' )
			;
			if ( resetSeqOnLogon ) {
				persistence.reset();
			}
			for ( FixField add : additionalFields ) {
				msg.addField(add);
			}
			send( msg );
			this.sessionStatus = SessionStatus.LOGON_SENT;
			return this;
		} finally {
			messagePool.release(msg);
		}
	}	
}
