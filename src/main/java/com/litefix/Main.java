package com.litefix;

import com.litefix.FixSession.FixSessionListener;
import com.litefix.models.FixField;
import com.litefix.models.FixGroup;
import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;
import com.litefix.models.MsgType;
import com.litefix.modules.impl.AsyncMessagesDispatcher;
import com.litefix.modules.impl.FixMessagePool;
import com.litefix.modules.impl.FixMessageValidator;
import com.litefix.modules.impl.SocketTransport;

public class Main {

	static ClientFixSession session;
	
	public static void main( String[] args ) throws Exception {
		FixMessagePool msgFactory = new FixMessagePool();
		
		FixSessionListener listener = new FixSessionListener() {

			@Override
			public void onConnection(boolean b) { System.out.println((b)?"Connected!":"Connection ERROR!");	}
			
			@Override
			public void onLogin() {	System.out.println("Logged IN"); }

			@Override
			public void onLogout() { System.out.println("Logged OUT"); }

			@Override
			public void onMessage(MsgType msgType, FixMessage msg) throws Exception {
				switch( msgType.toString() ) {
				case "S":
					handleQuote( msg );
				//	sendNewOrder( msgFactory );
					break;
				default:
					throw new Exception("Unsupported message");
				}				
			}			
		};

		session =(ClientFixSession)new ClientFixSession( listener )
				.withBeginString("FIX.4.4")
				.withSenderCompId("UFX2-DEV-A1-FIX")
				.withTargetCompId("EXCEED-FIX-INFO")
				.withResetSeqOnLogon( true )
				.withHbIntervalSec( 1 )
				.withResetSeqOnLogon( true )
				.withMessageFactory( msgFactory )
				.withTransport( new SocketTransport() )
				.withMessagesDispatcher( new AsyncMessagesDispatcher() )
				.withMessagesValidator( new FixMessageValidator(FixMessageValidator.CRC) )
				.doWarmup(5000);
		
		session.doConnect("hivex-fix.uat.svc.exceed-qa.internal.unicreditgroup.eu", 29435)
			   .doLogon( new FixField( 553, "UFX-UAT" ), new FixField( 554, "uat-test-q8f" ) )
		;
	}
	
	public static void sendNewOrder( FixMessagePool msgFactory ) throws Exception {
		FixMessage msg = null;
		try {
			msg = msgFactory.get().setMsgType("D")
				.addField( new FixTag(98), "0" )
				.addField( new FixTag(108), "");
			session.send(msg);
		} finally {
			msgFactory.release(msg);
		}		
	}		
	
	public static void handleQuote( FixMessage msg ) {
		String symbol = msg.getStringValue( FixTag.TAG_SYMBOL );
		String streamID = msg.getStringValue( new FixTag(10000));
		String bookID = msg.getStringValue( FixTag.TAG_QUOTE_ID );
	
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
	
}
