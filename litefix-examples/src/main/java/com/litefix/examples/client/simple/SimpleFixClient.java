package com.litefix.examples.client.simple;

import java.util.concurrent.TimeUnit;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.utils.FixUUID;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.examples.client.AbstractClient;
import com.litefix.models.dictionary.DefaultFix44Dictionary;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.AbstractFixSession;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

public class SimpleFixClient extends AbstractClient {

	private ClientFixSessionConfig sessionCfg;
	private IClientTransport transport;
	private IPersistence<FixMessageEncoder> persistence;
	private ClientFixSession session;
	
	public SimpleFixClient() {
		sessionCfg = new ClientFixSessionConfig()
			.setSenderCompId( "TESTSEND1" )
			.setTargetCompId( "TESTTARGET1" )
			.setHeartBtInt( 1 )
			.setResetSeqNumFlag('N')
			.addServer("localhost", 5178)
			.addServer("localhost", 5179)
			.setDictionary( DefaultFix44Dictionary.init() );
		
		transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		persistence = new InMemoryPersistence<FixMessageEncoder>();
	}
	
	public void start() throws Exception {
		initLogging();
		
		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( true, true );
	}	
	
	public static void main( String[] args ) throws Exception {
		new SimpleFixClient().start();
	}
	
	// IFixMessageListener
	@Override
	public void onMessageRcv(FixMessageDecoder decoder, AbstractFixSession session) throws BusinessRejectMessageException {
		switch (decoder.getMsgType()) {
		case "S" : handleQuote(decoder); break;
		
		default:
			long now = System.nanoTime();
			System.out.println("["+TimeUnit.NANOSECONDS.toMicros(now-decoder.getRcvNanoTime())+"]micros -> onMessage:"+new String(decoder.getMsgBuff()));
			break;
		}
	}

	@Override
	public void onLogout(FixMessageDecoder decoder, AbstractFixSession session) {
		System.out.println("logged out..");
	}
	
	@Override
	public void onConnect(boolean upOrDown) {
		System.out.println("connected:"+upOrDown);
	}

	@Override
	public void onLogon(FixMessageDecoder decoder, AbstractFixSession session, boolean result) throws SessionRejectMessageException, BusinessRejectMessageException {
		if (result) {
			System.out.println("logged in");
			for (int i=0; i<1; i++ ) {
				long startTime = System.nanoTime();
				FixMessageEncoder enc = session.newEncoder( "D" )
					.set(55, "IT0000000000")	// Symbol
					.set(11, FixUUID.random() ) // ClOrdID
				//	.set(15, "EUR" ) // Currency
					.set(21, '1' )// HandlInst
					.set(38, 1000 )// OrderQty
					.set(40, '1' )// OrdType - market
					.set(54, '1' )// Side
					.set(60, TimeUtils.getSendingTime() )// TransactTime
				;
				session.sendMessage( enc );
				long now = System.nanoTime();
				System.out.println("["+TimeUnit.NANOSECONDS.toMicros(now-startTime)+"]micros -> send done");
			}
		} else {
			System.out.println("logged out");
		}
	}
	
	public void handleQuote( FixMessageDecoder decoder ) {	
	}

}
