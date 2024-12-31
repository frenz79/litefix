package com.litefix.modules.transport;

public interface IClientTransport extends ITransport {

	void connect(String host, int port, ITransportListener listener) throws Exception;
	
}
