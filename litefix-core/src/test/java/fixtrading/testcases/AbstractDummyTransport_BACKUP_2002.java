package fixtrading.testcases;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

<<<<<<< HEAD
=======
import com.litefix.models.session.ProxySettings;
>>>>>>> origin/develop
import com.litefix.models.session.SSLSettings;
import com.litefix.modules.transport.IClientTransport;
import com.litefix.modules.transport.ITransportListener;

public abstract class AbstractDummyTransport implements IClientTransport {

	ITransportListener listener;
	AtomicInteger sendCount = new AtomicInteger(0);
	CountDownLatch latch;
	
	public AbstractDummyTransport(CountDownLatch l) {
		this.latch = l;
	}
	
	@Override
	public void shutdown() throws IOException {
		new Thread(() -> { listener.onConnect(false);}).start();
	}

	@Override
	public void disconnect() throws Exception {
		new Thread(() -> { listener.onConnect(false);}).start();
	}
	
	void respond( String msg ) {
		new Thread(() -> {listener.onMessage(msg.getBytes(), 0, msg.getBytes().length, 0);}).start();
	}

	@Override
<<<<<<< HEAD
	public void connect(String host, int port, SSLSettings sslSettings, ITransportListener listener) throws Exception {
=======
	public void connect(String host, int port, SSLSettings sslSettings, ProxySettings proxy, ITransportListener listener) throws Exception {
>>>>>>> origin/develop
		this.listener = listener;
		listener.onConnect(true);
	}
}
