package com.litefix;

import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;

public interface IFixSessionListener {
	public void onLogout(FixMessage msg);
	public void onMessage(MsgType sgType, FixMessage msg) throws Exception;
	public void onConnection(boolean upOrDown);
	public void onLoginSuccess(FixMessage msg);
	public void onLoginFailed(FixMessage msg);
}
