package fixtrading.testcases;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

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
	public void stop() throws IOException {
		new Thread(() -> { listener.onConnect(false);}).start();
	}

	void respond( String msg ) {
		new Thread(() -> {listener.onMessage(msg.getBytes(), 0, msg.getBytes().length, 0);}).start();
	}
	
	@Override
	public boolean connect(String host, int port, ITransportListener listener) throws Exception { 
		this.listener = listener;
		listener.onConnect(true);
		return true;
	}
}
