package com.litefix;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.litefix.FixSession.FixSessionListener;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;
import com.litefix.modules.ITransport;

@TestInstance(Lifecycle.PER_CLASS)
public class FixSessionTest {

	public static class DummyTransport implements ITransport {

		@Override
		public void stop() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public ITransport send(FixMessage msg) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ITransport connect(String host, int port) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean pollMessage(ByteBuffer targetBuff, byte[] beginMessage) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	@Test
	public void sendHeartbeatTest() throws Exception {
		String serverHost = "mock_server";
		int serverPort = 1234;
		String senderCompId ="mock_senderCompId";
		String targetCompId = "mock_targetCompId";
				
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
				//	handleQuote( msg );
				//	sendNewOrder( msgFactory );
					break;
				default:
					throw new Exception("Unsupported message");
				}				
			}			
		};

		ClientFixSession session =(ClientFixSession)new ClientFixSession( listener )
				.withBeginString(FixSession.BEGIN_STRING_FIX44)
				.withSenderCompId(senderCompId)
				.withTargetCompId(targetCompId)
				.withTransport(new DummyTransport())
				.validate()
				.doWarmup(5000);
		
		session.doConnect(serverHost, serverPort).doLogon( );
	}
}
