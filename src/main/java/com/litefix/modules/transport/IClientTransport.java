package com.litefix.modules.transport;

import com.litefix.models.session.SSLSettings;

public interface IClientTransport extends ITransport {

	boolean connect(String host, int port, SSLSettings sslSettings, ITransportListener listener) throws Exception;
	
	public default boolean connect(String host, int port, final ITransportListener listener ) throws Exception {	
		return connect( host, port, null, listener );
	}
}
