package com.litefix;

import com.litefix.commons.IFixConst;
import com.litefix.models.FixGroup;
import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;
import com.litefix.models.MsgType;
import com.litefix.modules.impl.DefaultFixMessagePool;

public class SimpleFixClient {

static ClientFixSession session;
	
	public static void main( String[] args ) throws Exception {
		String serverHost = "";
		int serverPort = 0;
		String senderCompId ="";
		String targetCompId = "";
				
		IFixSessionListener listener = new IFixSessionListener() {

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
				.withBeginString(IFixConst.BEGIN_STRING_FIX44)
				.withSenderCompId(senderCompId)
				.withTargetCompId(targetCompId)
				.validate()
				.doWarmup(5000);
		
		session.doConnect(serverHost, serverPort).doLogon( );
	}
	
	public static void sendNewOrder( DefaultFixMessagePool msgFactory ) throws Exception {
		FixMessage msg = null;
		try {
			msg = msgFactory.get().setMsgType("D")
				.addField( IFixConst.TAG_98, "0" )
				.addField( IFixConst.TAG_108, "");
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
