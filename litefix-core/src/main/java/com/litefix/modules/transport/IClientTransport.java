package com.litefix.modules.transport;

import com.litefix.models.session.SSLSettings;

public interface IClientTransport extends ITransport {

	
	void connect(String host, int port, SSLSettings sslSettings, ITransportListener listener) throws Exception;
	
	public default void connect(String host, int port, final ITransportListener listener ) throws Exception {	
		connect( host, port, null, listener );
	}
}
