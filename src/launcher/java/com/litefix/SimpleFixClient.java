package com.litefix;

import java.util.concurrent.TimeUnit;

import com.litefix.commons.IFixConst;
import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.BusinessRejectMessageException.BUSINESS_REJECT_REASON;
import com.litefix.commons.utils.FixUUID;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.models.FixGroup;
import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;
import com.litefix.models.MsgType;

public class SimpleFixClient {

static ClientFixSession session;
	
	public static void main( String[] args ) throws Exception {
		String serverHost = "localhost";
		int serverPort = 5179;
		String senderCompId ="TESTSEND1";
		String targetCompId ="TESTTARGET1";
				
		IFixSessionListener listener = new IFixSessionListener() {

			@Override
			public void onConnection(boolean b) { System.out.println((b)?"Connected!":"Connection ERROR!");	}
			
			@Override
			public void onLogout(FixMessage msg) { System.out.println("Logged OUT"); }

			@Override
			public void onMessage(MsgType msgType, FixMessage msg) throws BusinessRejectMessageException {
				switch( msgType.toString() ) {
				case "S":
					handleQuote( msg );
				//	sendNewOrder( msgFactory );
					break;
				case "D":
					break;
				case "3":
					break;
				default:
					throw new BusinessRejectMessageException(
						msg.getHederField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(),
						"35",
						msgType.toString(),
						BUSINESS_REJECT_REASON.UNSUPPORTED_MESSAGE_TYPE,
						"Unsupported message"
					);
				}				
			}

			@Override
			public void onLogin(FixMessage msg, boolean result) {
				int batchSize = 10;
				//warmup
				/*
				for ( int i=0; i<batchSize; i++ ) {
					try { 
						sendNewOrder();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				*/
				long startTime = System.nanoTime();
				for ( int i=0; i<batchSize; i++ ) {
					try { 
						sendNewOrder();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				long endTime = System.nanoTime();
				
				System.out.println("Average NewOrder send time="+(TimeUnit.NANOSECONDS.toMicros(endTime-startTime)/batchSize));
			}			
		};

		session =(ClientFixSession)new FixSessionBuilder(serverHost, serverPort, listener )
				.withBeginString(IFixConst.BEGIN_STRING_FIX44)
				.withSenderCompId(senderCompId)
				.withTargetCompId(targetCompId)
				.withHbIntervalSec(30)
		//		.withResetSeqOnDisconnect(false)
		//		.withResetSeqOnLogon(false)
				.withLogonTimeoutSec(5)
				.withAutomaticLogonOnConnect(true)
				.withAutomaticLogonOnLogout(true)
				.withAutomaticReconnect(true, 500L)
				.build();
				
		session.doConnectAndRetry( 1000L );
	}
	
	public static void sendNewOrder( ) throws Exception {
		FixMessage msg = null;
		try {
			msg = session.getMessagePool().get().setMsgType("D")
				.addField( IFixConst.Symbol, "IT0000000000" )
				.addField( IFixConst.ClOrdID, FixUUID.random() )
				.addField( IFixConst.Currency, "EUR" )
				.addField( IFixConst.HandlInst, '1' )
				.addField( IFixConst.OrderQty, "1000" )
				.addField( IFixConst.OrdType, '1' )
				.addField( IFixConst.Side, '1' )
				.addField( IFixConst.TransactTime, TimeUtils.getSendingTime() );		
				
			session.send(msg);
		} finally {
			session.getMessagePool().release(msg);
		}		
	}		
	
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
}
