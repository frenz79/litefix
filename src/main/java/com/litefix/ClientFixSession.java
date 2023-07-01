package com.litefix;

import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;

public class ClientFixSession extends FixSession {

	public ClientFixSession(FixSessionListener fixSessionListener) {
		super(fixSessionListener);
	}

	public ClientFixSession doConnect( String host, int port ) throws Exception {
		transport.connect( host, port );
		fixSessionListener.onConnection( true );
		Thread loopTh = new Thread(() -> {
			try {
				runLoop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		loopTh.setName("FixSession["+senderCompId+"->"+targetCompId+"]-Loop");
		loopTh.start();
		return this;
	}
	
	public ClientFixSession doLogon( ) throws Exception {
		return doLogon( new FixField[0] );
	}

	public ClientFixSession doLogon( FixField ... additionalFields ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("A")
				.addField( new FixTag(98), "0" ) // EncryptMethod
				.addField( new FixTag(108), hbIntervalSec )
				.addField( new FixTag(141), (resetSeqOnLogon)?'Y':'N' )		
			;
			for ( FixField add : additionalFields ) {
				msg.addField(add);
			}
			send( msg );
			this.sessionStatus = Status.PENDING;
			return this;
		} finally {
			messagePool.release(msg);
		}
	}	
}
