package com.litefix.models.session;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;

public interface IFixMessageListener {

	public void onMessage(FixMessageDecoder decoder) throws BusinessRejectMessageException;
	
}
