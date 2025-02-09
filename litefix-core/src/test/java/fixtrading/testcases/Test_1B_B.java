package fixtrading.testcases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.AbstractFixSession;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.IFixSessionListener;
import com.litefix.models.session.SSLSettings;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.IClientTransport;
import com.litefix.modules.transport.ITransportListener;

/**
 * https://www.fixtrading.org/standards/fix-session-testcases-online/ 
 */
public class Test_1B_B extends AbstractTest {	
	
	@Test
	// Send Logon(35=A) request message. ->	Logon(35=A) acknowledgement message sent by peer.
	public void baseTest() throws Exception {
		CountDownLatch latch = new CountDownLatch(2);
		
		ClientFixSessionConfig sessionCfg = getSessionConfig();
		
		IClientTransport transport = new AbstractDummyTransport( latch ) {
			@Override
			public boolean send(byte[] buffer) throws IOException {	
				respond("8=FIX.4.49=7935=A49=targetCompId56=senderCompId52=2023010134=00143=N98=0108=10141=Y10=094");
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
				public void onLogon(FixMessageDecoder decoder, AbstractFixSession s, boolean result)	throws SessionRejectMessageException, BusinessRejectMessageException {
					latch.countDown();
				}

				@Override
				public void onConnect(boolean upOrDown) {
					latch.countDown();
				}
			})
		;
				
		session.doConnect( true, true );
		latch.await(5, TimeUnit.SECONDS);
		assertEquals(0, latch.getCount() );
	}
}
