package com.litefix.modules.transport;

import java.io.IOException;

public interface ITransport {
	

	void disconnect() throws Exception;
	
	void shutdown() throws IOException;

	boolean send(byte[] buffer) throws Exception;

}
