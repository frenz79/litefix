package com.litefix.models.session;

import java.util.Timer;
import java.util.TimerTask;

import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.transport.IClientTransport;

public class ClientFixSession extends AbstractFixSession {

	private int fixServerSiteId = -1;
	
	private boolean autoLogin = true;
	private boolean autoReconnect = true;
	private long reconnectWaitTime = 1000l; 
	private long reloginWaitTime = 1000l; 
	
	public ClientFixSession(IClientTransport transport, IPersistence<FixMessageEncoder> persistence, ClientFixSessionConfig sessionConfig ) {
		super(transport, persistence, sessionConfig);
		LOGGER_SESSION.info("ClientFixSession initialized with sessionConfig:{}", sessionConfig);
	}
	
	public AbstractFixSession doConnect( ) throws Exception {
		return doConnect( false, false );
	}

	public AbstractFixSession doConnect( boolean autoLogin, boolean autoReconnect ) throws Exception {
		this.autoLogin = autoLogin;
		this.autoReconnect = autoReconnect;
		scheduleReconnect( 0l );
		return this;
	}

	public synchronized ClientFixSession doLogon( FixMessageEncoder encoder ){
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
		//  Propagate to listeners
		getSessionListener().onConnect(status);

		if ( !status && autoReconnect ) {
			scheduleReconnect(reconnectWaitTime);
		}
		if ( status && autoLogin ) {
			scheduleRelogin(reloginWaitTime);
		}
	}

	private void scheduleReconnect( long delay ) {
		this.fixServerSiteId = (++fixServerSiteId)%sessionConfig.getServerHosts().size();
		String host = sessionConfig.getServerHosts().get(fixServerSiteId).getName();
		int port = sessionConfig.getServerHosts().get(fixServerSiteId).getPort();
		
		TimerTask task = new TimerTask() {
			public void run() {
				LOGGER_SESSION.info("Trying connect to {}:{}", host, port);
				try {
					if (!((IClientTransport)getTransport( )).connect( host, port, sessionConfig.getSslSettings(), ClientFixSession.this )) {
						scheduleReconnect(reconnectWaitTime);
					}
				} catch (Exception e) {
					LOGGER_SESSION.error("Connect failed:{}",e.getMessage());
					scheduleReconnect(reconnectWaitTime);
				}
			}
		};
		Timer timer = new Timer("ConnectTimer-"+sessionConfig.getSenderCompId()+"->"+sessionConfig.getTargetCompId());
		timer.schedule(task, delay);
	}
	
	private void scheduleRelogin( long delay ) {
		TimerTask task = new TimerTask() {
			public void run() {
				LOGGER_SESSION.warn("Trying login..");
				try {
					doLogon();
				} catch (Exception e) {
					LOGGER_SESSION.error("Login failed:{}",e.getMessage());
				}
			}
		};
		Timer timer = new Timer("LoginTimer-"+sessionConfig.getSenderCompId()+"->"+sessionConfig.getTargetCompId());
		timer.schedule(task, delay);
	}

	// IClientTransportListener
	@Override
	public void onError(Throwable cause) {
		LOGGER_SESSION.error("onError:{}",cause.getMessage());
	}

	@Override
	FixMessageEncoder beforeSend(FixMessageEncoder encoder) {
		IFixMessageListener listener = getMessageListener(encoder.getMsgType());
		if ( listener!=null ) {
			return listener.beforeMessageSnd(encoder);
		}
		return encoder;
	}
}
