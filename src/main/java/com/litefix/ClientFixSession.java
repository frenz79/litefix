package com.litefix;

import com.litefix.commons.IFixConst;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.SessionStatus;

public class ClientFixSession extends FixSession {

	private final String host;
	private final int port;
	
	public ClientFixSession(String host, int port, IFixSessionListener fixSessionListener) {
		super(fixSessionListener);
		this.host = host;
		this.port = port;
	}

	public ClientFixSession doConnect( ) throws Exception {
		transport.connect( host, port );
		fixSessionListener.onConnection( true );
		startMainLoop();
		if ( automaticLogonOnConnect ) {
			doLogon();
		}
		return this;
	}
	
	public ClientFixSession doConnectAndRetry( long retryWaitTimeMillis ) throws Exception {
		while( true ) {
			try {
				transport.connect( host, port );
				break;
			} catch ( Exception ex1 ) {
				System.out.println("Connect failed:"+ ex1);
				Thread.sleep(retryWaitTimeMillis);
			}
		}
		
		fixSessionListener.onConnection( true );
		startMainLoop();
		if ( automaticLogonOnConnect ) {
			doLogon();
		}
		return this;
	}
	
	private void startMainLoop() {		
		Thread loopTh = new Thread(() -> {
			try {
				messagePoller();
			} catch ( Exception ex ) {
				// Disconnection here ?
				ex.printStackTrace();
				if (resetSeqOnDisconnect) {
					persistence.reset();
				}
				sessionStatus = SessionStatus.DISCONNECTED;
				fixSessionListener.onConnection( false );
				if ( automaticReconnect ) {
					while( true ) {
						try {
							doConnect();
							break;
						} catch ( Exception ex1 ) {
							System.out.println("Connect failed:"+ ex1);
							try {
								Thread.sleep(automaticReconnectRetryDelayMillis);
							} catch (InterruptedException e) {
								break;
							}							
						}						
					}
				}
			}
		});
		loopTh.setName("FixSession["+senderCompId+"->"+targetCompId+"]-Loop");
		loopTh.start();
	}
	
	public ClientFixSession doLogon( ) throws Exception {
		return doLogon( new FixField[0] );
	}

	public ClientFixSession doLogon( FixField ... additionalFields ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("A")
				.addField( IFixConst.Logon.EncryptMethod, "0" ) // EncryptMethod
				.addField( IFixConst.Logon.HeartBtInt, hbIntervalSec )
				.addField( IFixConst.Logon.ResetSeqNumFlag, (resetSeqOnLogon)?'Y':'N' )
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
