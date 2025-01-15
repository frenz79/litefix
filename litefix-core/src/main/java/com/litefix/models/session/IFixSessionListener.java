package com.litefix.models.session;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;

public interface IFixSessionListener {

	public void onLogout(FixMessageDecoder decoder);
	public void onLogon(FixMessageDecoder decoder, boolean result) throws SessionRejectMessageException,BusinessRejectMessageException;

	public void onConnect(boolean upOrDown);
	
}
