package com.litefix;

import java.util.concurrent.TimeUnit;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.utils.FixUUID;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.models.dictionary.DefaultFix44Dictionary;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.IFixMessageListener;
import com.litefix.models.session.IFixSessionListener;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

public class SimpleFixClient implements IFixMessageListener, IFixSessionListener {

	private ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig();
	private IClientTransport transport = new ClientSocketTransport();
	private ClientFixSession session;
	
	public SimpleFixClient() {
		sessionCfg.setSenderCompId( "TESTSEND1" );
		sessionCfg.setTargetCompId( "TESTTARGET1" );
		sessionCfg.setHeartBtInt( 0 );
		sessionCfg.setResetSeqNumFlag('Y');
		sessionCfg.setServerHost("localhost");
		sessionCfg.setServerPort( 5179);
		sessionCfg.dictionary = DefaultFix44Dictionary.init();
	}
	
	public void start() throws Exception {
		session = new ClientFixSession( transport, sessionCfg );
		session.addAllMessagesListener( this );
		session.addSessionListener( this );
		session.doConnect();
	}	
	
	public static void main( String[] args ) throws Exception {
		new SimpleFixClient().start();
	}
	
	// IFixMessageListener
	@Override
	public void onMessage(FixMessageDecoder decoder) throws BusinessRejectMessageException {
		switch (decoder.getMsgType()) {
		case "S" : handleQuote(decoder); break;
		
		default:
			long now = System.nanoTime();
			System.out.println("["+TimeUnit.NANOSECONDS.toMicros(now-decoder.getRcvNanoTime())+"]micros -> onMessage:"+new String(decoder.getMsgBuff()));
			break;
		}
	}

	@Override
	public void onLogout(FixMessageDecoder decoder) {
		session.doLogon();
	}
	
	@Override
	public void onConnect(boolean upOrDown) {
		if ( upOrDown ) {
			session.doLogon();
		} else {
			try {
				session.doConnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLogon(FixMessageDecoder decoder, boolean result) throws SessionRejectMessageException, BusinessRejectMessageException {
		if ( result ) {
			for (int i=0; i<10000; i++ ) {
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
			}
		}
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
