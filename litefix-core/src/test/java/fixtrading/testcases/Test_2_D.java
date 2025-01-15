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
	Garbled message received.
	 
	Consider garbled and ignore message (do not increment NextNumIn) and continue accepting messages.
	Generate a warning condition in test output.
 */
public class Test_2_D extends AbstractTest {	
	
	@Test
	public void baseTest() throws Exception {
		CountDownLatch latch = new CountDownLatch( 3 );
		
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
					
					// Send Garble 
					respond( "8=FIX.4.49=7935=X49=targetCompId56=senderCompId52=2023010134=00243=N112=Y10=054");
					
					try {	Thread.sleep(100);	} catch (InterruptedException e) {}
					
					// Send TestRequest 
					respond( "8=FIX.4.49=7935=149=targetCompId56=senderCompId52=2023010134=00243=N112=Y10=054");
				}
				// 35=0
				else if ( counter==1 ) {
					assertTrue( strMsg.contains("35=0") );
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
		latch.await(1, TimeUnit.SECONDS);
		assertEquals(0, latch.getCount() );
	}
}
