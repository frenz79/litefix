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
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.IFixSessionListener;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.IClientTransport;

/**
 * https://www.fixtrading.org/standards/fix-session-testcases-online/ 
 * 
	MsgSeqNum(34) lower than expected without PossDupFlag(43) set to Y.
	 Exception: SequenceReset(35=4).
	 
	Whenever possible it is recommended that FIX engine attempt to send a Logout(35=5) message with a text message of “MsgSeqNum too low, expecting X but received Y”.
	(Optional) Wait for Logout(35=5) message response (Note: likely will have inaccurate MsgSeqNum(34)) or wait 2 seconds, whichever comes first.
	Disconnect.
	Generate an error condition in test output.
 */
public class Test_2_C extends AbstractTest {	
	
	@Test
	public void baseTest() throws Exception {
		CountDownLatch latch = new CountDownLatch( 7 );
		
		ClientFixSessionConfig sessionCfg = getSessionConfig();
		
		IClientTransport transport = new AbstractDummyTransport( latch ) {
			
			@Override
			public boolean send(byte[] buffer) throws IOException {	
				String strMsg = new String(buffer);
				//System.out.println("Sending ->"+strMsg);
				int counter = sendCount.getAndIncrement();
				
				if ( counter==0 ) {
					// Send login ACK
					respond( "8=FIX.4.49=7935=A49=targetCompId56=senderCompId52=2023010134=00143=N98=0108=10141=Y10=094");
					
					try {	Thread.sleep(100);	} catch (InterruptedException e) {}
					
					// Send TestRequest 
					respond( "8=FIX.4.49=7935=149=targetCompId56=senderCompId52=2023010134=00243=N112=Y10=054");
					
					try {	Thread.sleep(100);	} catch (InterruptedException e) {}
					
					// Send TestRequest 
					respond( "8=FIX.4.49=7935=149=targetCompId56=senderCompId52=2023010134=00343=N112=Y10=055");
					
					try {	Thread.sleep(100);	} catch (InterruptedException e) {}
					
					// Send SequenceReset (with old seq)
					respond( "8=FIX.4.49=7935=449=targetCompId56=senderCompId52=2023010134=00143=N123=N36=410=010");
					
					try {	Thread.sleep(100);	} catch (InterruptedException e) {}
					
					// Send TestRequest (with old seq)
					respond( "8=FIX.4.49=7935=149=targetCompId56=senderCompId52=2023010134=00243=N112=Y10=054");
				}
				// 35=0
				else if ( counter==1 ) {
					assertTrue( strMsg.contains("35=0") );
					latch.countDown();
				}
				// 35=0
				else if ( counter==2 ) {
					assertTrue( strMsg.contains("35=0") );
					latch.countDown();
				}
				// 35=3
				else if ( counter==3 ) {
					assertTrue( strMsg.contains("35=3") );
					latch.countDown();
				}
				// 35=5
				else if ( counter==4 ) {
					assertTrue( strMsg.contains("35=5") );
					latch.countDown();
				}
				return true;
			}
		};
		
		ClientFixSession session = (ClientFixSession) new ClientFixSession( transport, new InMemoryPersistence<FixMessageEncoder>(), sessionCfg )
			.withSessionListener( new IFixSessionListener() {

				@Override
				public void onLogout(FixMessageDecoder decoder) {
					System.out.println("onLogout");
					latch.countDown();
				}

				@Override
				public void onLogon(FixMessageDecoder decoder, boolean result)	throws SessionRejectMessageException {
					System.out.println("onLogon");
					latch.countDown();
				}

				@Override
				public void onConnect(boolean upOrDown) {
					System.out.println("onConnect="+upOrDown);
					latch.countDown();
				}
			})
		;
				
		session.doConnect( true, false );
		latch.await(10000, TimeUnit.SECONDS);
		assertEquals(0, latch.getCount() );
	}
}
