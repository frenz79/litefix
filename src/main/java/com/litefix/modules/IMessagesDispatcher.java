package com.litefix.modules;

import com.litefix.FixSession;
import com.litefix.models.FixMessage;

public interface IMessagesDispatcher {

	void onAsyncMessage(FixMessage msg, FixSession fixSession);

}
