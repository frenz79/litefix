package com.litefix;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.litefix.commons.IFixConst;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;
import com.litefix.models.SessionStatus;
import com.litefix.modules.ITransport;
import com.litefix.modules.impl.AsyncMessagesDispatcher;

@TestInstance(Lifecycle.PER_CLASS)
public class FixSessionTest {

	public static class DummyTransport implements ITransport {

		@Override
		public void stop() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public ITransport send(FixMessage msg) throws IOException {
			System.out.println("-->"+msg.toString());
			return this;
		}

		boolean connectRcv = false;
		long firstMessageTime = 0L;
		@Override
		public ITransport connect(String host, int port) throws Exception {
			connectRcv  = true;
			firstMessageTime = System.currentTimeMillis() + 1000L;
			return null;
		}

		int msgCounter = 0;
		@Override
		public boolean pollMessage(ByteBuffer targetBuff, byte[] beginMessage) throws IOException {
			if (!connectRcv || System.currentTimeMillis()<firstMessageTime) return false;
			
			String msgResp = null;
			switch( msgCounter ) {
			case 0:
				msgResp = "8=FIX.4.49=9235=A34=149=FXCM50=U100D152=20120927-13:15:34.81056=fx1294946_client198=0108=30141=Y10=187";
				msgCounter++;
				break;
			case 1:
				msgResp = "8=FIX.4.49=6435=049=FXCM56=fx1294946_client152=20230701-20:54:53.32734=210=0998=5141=Y10=127";
				msgCounter++;
				break;
			}
			
			if ( msgResp!=null ) {
				System.arraycopy(msgResp.getBytes(), 0,	targetBuff.array(), 0, msgResp.getBytes().length);
				targetBuff.limit(msgResp.getBytes().length);
			}
			
			return msgResp!=null;
		}
	}
	
	public static class DummyFixSessionListener implements IFixSessionListener {
		@Override
		public void onConnection(boolean b) { System.out.println((b)?"Connected!":"Connection ERROR!");	}

		@Override
		public void onLogout(FixMessage msg) { System.out.println("Logged OUT"); }

		@Override
		public void onMessage(MsgType msgType, FixMessage msg) throws Exception {
			switch( msgType.toString() ) {
			case "S":
			//	handleQuote( msg );
			//	sendNewOrder( msgFactory );
				break;
			default:
				throw new Exception("Unsupported message");
			}				
		}

		@Override
		public void onLoginSuccess(FixMessage msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoginFailed(FixMessage msg) {
			// TODO Auto-generated method stub
			
		}	
	}
	
	
	// @Test
	public void sendHeartbeatTest() throws Exception {
		String serverHost = "mock_server";
		int serverPort = 1234;
		String senderCompId ="fx1294946_client1";
		String targetCompId = "FXCM";
				
		IFixSessionListener listener = new IFixSessionListener() {

			@Override
			public void onConnection(boolean b) { System.out.println((b)?"Connected!":"Connection ERROR!");	}

			@Override
			public void onLogout(FixMessage msg) { System.out.println("Logged OUT"); }

			@Override
			public void onMessage(MsgType msgType, FixMessage msg) throws Exception {
				switch( msgType.toString() ) {
				case "S":
				//	handleQuote( msg );
				//	sendNewOrder( msgFactory );
					break;
				default:
					throw new Exception("Unsupported message");
				}				
			}

			@Override
			public void onLoginSuccess(FixMessage msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoginFailed(FixMessage msg) {
				// TODO Auto-generated method stub
				
			}			
		};

		ClientFixSession session =(ClientFixSession)new FixSessionBuilder( serverHost, serverPort, listener )
				.withBeginString(IFixConst.BEGIN_STRING_FIX44)
				.withSenderCompId(senderCompId)
				.withTargetCompId(targetCompId)
				.withTransport(new DummyTransport())
				.withMessagesDispatcher(new AsyncMessagesDispatcher())
				.withHbIntervalSec(5)
				.build();
		
		session.doConnect( ).doLogon( );
		
		while(true) {}
	}
	
	@Test
	public void testPersistence() throws Exception {
		ClientFixSession session =(ClientFixSession)new FixSessionBuilder( "", 0,new DummyFixSessionListener() )
				.withBeginString(IFixConst.BEGIN_STRING_FIX44)
				.withSenderCompId("senderCompId")
				.withTargetCompId("targetCompId")
				.withTransport(new DummyTransport())
				.withMessagesDispatcher(new AsyncMessagesDispatcher())
				.withHbIntervalSec(5)
				.build();
		// Force active status
		session.setSessionStatus(SessionStatus.ACTIVE);
		
		sendNewOrder( session );
		sendNewOrder( session );
		sendNewOrder( session );
		sendNewOrder( session );
		sendNewOrder( session );
		sendNewOrder( session );
		
		System.out.println("------> processResendRequest()");
		
		((FixSession)session).getSessionMessageHanlder().processResendRequest( 
			session.getMessageFactory().get().setMsgType("2")
				.addField( IFixConst.BeginSeqNo, "0" )
				.addField( IFixConst.EndSeqNo, "4" )
		);
	}
	
	public static void sendNewOrder( FixSession session ) throws Exception {
		FixMessage msg = null;
		try {
			msg = session.getMessageFactory().get().setMsgType("D")
				.addField( IFixConst.TAG_98, "0" )
				.addField( IFixConst.TAG_108, "");
			session.send(msg);
		} finally {
			session.getMessageFactory().release(msg);
		}		
	}	
}
