package com.litefix.modules.transport;

public interface ITransportListener {

	public void onConnect( boolean status );

	public void onMessage(byte[] buffer, int from, int len, long rcvNanoTime);

	public void onError(Throwable cause);

}
