package com.litefix.modules;

import com.litefix.FixSession;
import com.litefix.models.FixMessage;

public interface IFixMessageDispatcher {

	void dispatch(FixMessage msg, FixSession fixSession);

}
