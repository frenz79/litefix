package com.litefix.models.session;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;

public interface IFixSessionListener {

	public void onLogout(FixMessageDecoder decoder, AbstractFixSession session);
	public void onLogon(FixMessageDecoder decoder, AbstractFixSession session, boolean result) throws SessionRejectMessageException,BusinessRejectMessageException;

	public void onConnect(boolean upOrDown);
	
}
