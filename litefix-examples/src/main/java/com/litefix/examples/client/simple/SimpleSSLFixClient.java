package com.litefix.examples.client.simple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.litefix.models.session.SSLSettings;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

public class SimpleSSLFixClient extends AbstractClient {

	private ClientFixSessionConfig sessionCfg;
	private IClientTransport transport;
	private IPersistence<FixMessageEncoder> persistence;
	private ClientFixSession session;
	
	public SimpleSSLFixClient() throws FileNotFoundException {
		sessionCfg = new ClientFixSessionConfig()
			.setSenderCompId( "TESTSEND1" )
			.setTargetCompId( "TESTTARGET1" )
			.setHeartBtInt( 1 )
			.setResetSeqNumFlag('Y')
			.addServer("localhost", 6179)
			.enableSSL( new SSLSettings()
				.setKeyStoreCert( new FileInputStream("D:\\SourceCode\\Incubator\\litefix\\litefix-examples\\src\\main\\resources\\ssl\\echo_server\\host.jks" ))
				.setTrustStoreCert( new FileInputStream("D:\\SourceCode\\Incubator\\litefix\\litefix-examples\\src\\main\\resources\\ssl\\echo_server\\host.jks" ))
				.setKeyStorePwd("password")
				.setTrustStorePwd("password")
				.setUseInsecureTrustManager(true)
			)
			.setDictionary( DefaultFix44Dictionary.init() );
		
		transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		persistence = new InMemoryPersistence<FixMessageEncoder>();
	}
	
	public void start() throws Exception {
		System.setProperty("javax.net.debug", "ssl,handshake");
		
		initLogging();
	    
		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( true, true );
	}	

	public static void main( String[] args ) throws Exception {
		new SimpleSSLFixClient().start();
	}
	
	// IFixMessageListener
	@Override
	public void onMessageRcv(FixMessageDecoder decoder, AbstractFixSession s) throws BusinessRejectMessageException {
		switch (decoder.getMsgType()) {
		case "S" : handleQuote(decoder); break;
		
		default:
			long now = System.nanoTime();
			System.out.println("["+TimeUnit.NANOSECONDS.toMicros(now-decoder.getRcvNanoTime())+"]micros -> onMessage:"+new String(decoder.getMsgBuff()));
			break;
		}
	}

	@Override
	public void onLogout(FixMessageDecoder decoder, AbstractFixSession s) {
		System.out.println("logged out..");
	}
	
	@Override
	public void onConnect(boolean upOrDown) {
		System.out.println("connected:"+upOrDown);
	}

	@Override
	public void onLogon(FixMessageDecoder decoder, AbstractFixSession s, boolean result) throws SessionRejectMessageException, BusinessRejectMessageException {
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
		
		/*if ( result ) {
			for (int i=0; i<10000; i++ ) {
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
		}
		*/
	}
	
	public void handleQuote( FixMessageDecoder decoder ) {
		
		
	}
	/*
	
	public static void handleQuote( FixMessage msg ) {
		String symbol = msg.getStringValue( IFixConst.Symbol );
		String streamID = msg.getStringValue( new FixTag(10000));
		String bookID = msg.getStringValue( IFixConst.QuoteID );
	
		System.out.println("streamID:  " + streamID + " |   bookID: " + bookID);
		
		handleLeg( msg, msg.getFixGroup( new FixTag(10555) ) ); // XCDNoNearBands
		handleLeg( msg, msg.getFixGroup( new FixTag(10556) ) ); // XCDNoFarBands
		System.out.println("---------------------------------------------------------------");
	}
	
	private static void handleLeg( FixMessage msg, FixGroup group ) {
		for ( int i=0; i<group.getCounter(); i++ ) {
			String LegBidPx = msg.getStringValue(group, new FixTag(681));
			String LegOfferPx = msg.getStringValue(group, new FixTag(684));
			String XCDLegBidSpotRate = msg.getStringValue(group, new FixTag(10001));
			String XCDLegBidFwdPoints = msg.getStringValue(group, new FixTag(10002));
			String XCDLegOfferSpotRate = msg.getStringValue(group, new FixTag(10003));
			String XCDLegOfferFwdPoints = msg.getStringValue(group, new FixTag(10004));
			String XCDLegBidQuoteType = msg.getStringValue(group, new FixTag(10005));
			String XCDLegOfferQuoteType = msg.getStringValue(group, new FixTag(10006));
			String XCDLegBidQty = msg.getStringValue(group, new FixTag(10007));
			String XCDLegOfferQty = msg.getStringValue(group, new FixTag(10008));
			String XCDLegBidFwdMargin = msg.getStringValue(group, new FixTag(10013));
			String XCDLegBidSpotMargin = msg.getStringValue(group, new FixTag(10014));
			String XCDLegOfferFwdMargin = msg.getStringValue(group, new FixTag(10015));
			String XCDLegOfferSpotMargin = msg.getStringValue(group, new FixTag(10016));
			
			System.out.println(LegBidPx + " :  " + XCDLegBidQty + " | "+  XCDLegOfferQty + " : " + LegOfferPx);
		}
	}
	*/
}
