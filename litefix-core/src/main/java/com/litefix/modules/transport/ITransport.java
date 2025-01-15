package com.litefix.modules.transport;

import java.io.IOException;

public interface ITransport {
	
	void stop() throws IOException;

	boolean send(byte[] buffer) throws IOException;

}
