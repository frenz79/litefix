package fixtrading.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
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
public class Test_1B_D extends AbstractTest {	
	
	@Test
	// d. Invalid Logon(35=A) acknowledgement message received.	
	// Generate an error condition in test output.
	// (Optional) Send Reject(35=3) message with RefSeqNum(45) identifying Logon(35=A) message’s MsgSeqNum(34) and Text(58) referencing error condition.
	// Send Logout(35=5) message with Text(58) referencing error condition.
	// Disconnect.
	public void baseTest() throws Exception {
		CountDownLatch latch = new CountDownLatch(5);
		
		ClientFixSessionConfig sessionCfg = getSessionConfig();
		
		IClientTransport transport = new AbstractDummyTransport( latch ) {
			
			@Override
			public boolean send(byte[] buffer) throws IOException {	
				String strMsg = new String(buffer);
				int counter = sendCount.getAndIncrement();
				
				if ( counter==0 ) {
					// Login ACK
					respond( "8=FIX.4.49=7935=A49=targetCompId56=senderCompId52=2023010134=143=N98=0108=10141=Y10=254" );
					latch.countDown();
				} else if ( counter==1 ) {
					assertTrue( strMsg.contains("35=3") );
					assertTrue( strMsg.contains("45=1") );
					assertTrue( strMsg.contains("34=2") );
					assertTrue( strMsg.contains("58=Something went wrong with login") );
					latch.countDown();
				} else if ( counter==2 ) {
					assertTrue( strMsg.contains("35=5") );
					assertTrue( strMsg.contains("Something went wrong with login") );
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
					latch.countDown();
				}

				@Override
				public void onLogon(FixMessageDecoder decoder, AbstractFixSession s, boolean result)	throws SessionRejectMessageException {
					throw new SessionRejectMessageException(
						decoder, 
						35,
						SESSION_REJECT_REASON.OTHER,
						"Something went wrong with login"
					);
				}

				@Override
				public void onConnect(boolean upOrDown) {
					latch.countDown();
				}
			})
		;
				
		session.doConnect( true, false );
		latch.await(5, TimeUnit.SECONDS);
		assertEquals(0, latch.getCount() );
	}
}
