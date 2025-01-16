package com.litefix.models.session;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;

public interface IFixMessageListener {

	public void onMessageRcv(FixMessageDecoder decoder) throws BusinessRejectMessageException;
	
	public default FixMessageEncoder beforeMessageSnd(FixMessageEncoder encoder) {return encoder;};
}
