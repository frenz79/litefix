package com.litefix;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;

public interface IFixSessionListener {
	public void onLogout(FixMessage msg);
	public void onMessage(MsgType sgType, FixMessage msg) throws BusinessRejectMessageException;
	public void onConnection(boolean upOrDown);
	public void onLogin(FixMessage msg, boolean result) throws SessionRejectMessageException,BusinessRejectMessageException;
}
