package com.litefix;

import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;

public interface IFixSessionListener {
	public void onLogin();
	public void onLogout();
	public void onMessage(MsgType sgType, FixMessage msg) throws Exception;
	public void onConnection(boolean b);
}
