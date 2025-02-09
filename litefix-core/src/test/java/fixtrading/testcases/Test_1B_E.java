package fixtrading.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.AbstractFixSession;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.IFixSessionListener;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.IClientTransport;

/**
 * https://www.fixtrading.org/standards/fix-session-testcases-online/ 
 */
public class Test_1B_E extends AbstractTest {	
	
	@Test
	// e. Receive any message other than a Logon(35=A) message.	
	// Log an error “first message not a logon”
	// (Optional) Send Reject(35=3) message with RefSeqNum(45) identifying message’s MsgSeqNum(34) and Text(58) referencing error condition.
	// (Optional) Send Logout(35=5) message with Text(58) referenceing error condition.
	// Disconnect.
	public void baseTest() throws Exception {
		CountDownLatch latch = new CountDownLatch(2);
		
		ClientFixSessionConfig sessionCfg = getSessionConfig();
		
		IClientTransport transport = new AbstractDummyTransport( latch ) {
			
			@Override
			public boolean send(byte[] buffer) throws IOException {	
				String strMsg = new String(buffer);
				System.out.print("Sending ->"+strMsg);
				int counter = sendCount.getAndIncrement();
				
				if ( counter==0 ) {
					// Send another message than login ACK
					respond( "8=FIX.4.49=7935=149=targetCompId56=senderCompId52=2023010134=143=N98=0108=10141=Y10=238" );
				} else if ( counter==1 ) {
					assertTrue( strMsg.contains("35=3") );
					assertTrue( strMsg.contains("45=1") );
					assertTrue( strMsg.contains("34=2") );
					assertTrue( strMsg.contains("Discarded msgType") );
					latch.countDown();
				} else if ( counter==2 ) {
					assertTrue( strMsg.contains("35=5") );
					assertTrue( strMsg.contains("Discarded msgType") );
					latch.countDown();
				}
				return true;
			}
		};
		
		ClientFixSession session = (ClientFixSession) new ClientFixSession( transport, new InMemoryPersistence<FixMessageEncoder>(), sessionCfg )
			.withSessionListener( new IFixSessionListener() {

				@Override
				public void onLogout(FixMessageDecoder decoder, AbstractFixSession s) {
					System.out.println("onLogout");
				}

				@Override
				public void onLogon(FixMessageDecoder decoder, AbstractFixSession s, boolean result)	throws SessionRejectMessageException {
					System.out.println("onLogon");
				}

				@Override
				public void onConnect(boolean upOrDown) {
					System.out.println("onConnect="+upOrDown);
					latch.countDown();
				}
			})
		;
				
		session.doConnect( true, false );
		latch.await(5, TimeUnit.SECONDS);
		assertEquals(0, latch.getCount() );
	}
}
