package com.litefix.modules;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.litefix.models.FixMessage;

public interface ITransport {

	public static interface TransportListener {
		void onMessage( FixMessage msg );
	}

	void stop() throws IOException;

	ITransport send(FixMessage msg) throws IOException;

	ITransport connect(String host, int port) throws Exception;

	boolean pollMessage(ByteBuffer targetBuff, byte[] beginMessage) throws IOException;
}
