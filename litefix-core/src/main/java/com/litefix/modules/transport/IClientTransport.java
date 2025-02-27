package com.litefix.modules.transport;

import com.litefix.models.session.ProxySettings;
import com.litefix.models.session.SSLSettings;

public interface IClientTransport extends ITransport {
	
	public default void connect(String host, int port, final ITransportListener listener ) throws Exception {	
		connect( host, port, null, null, listener );
	}	

	public default void connect(String host, int port, SSLSettings sslSettings, ITransportListener listener) throws Exception {
		connect( host, port, sslSettings, null, listener );
	}

	void connect(String host, int port, SSLSettings sslSettings, ProxySettings proxySettings, ITransportListener listener) throws Exception;
}
