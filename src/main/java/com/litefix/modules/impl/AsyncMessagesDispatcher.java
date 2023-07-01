package com.litefix.modules.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.litefix.FixSession;
import com.litefix.models.FixMessage;
import com.litefix.modules.IMessagesDispatcher;

public class AsyncMessagesDispatcher implements IMessagesDispatcher {

	private Executor executor = Executors.newSingleThreadExecutor();
	
	@Override
	public void dispatch(FixMessage msg, FixSession fixSession) {
		executor.execute(() -> {
			fixSession.dispatch(msg, fixSession);			
		});
	}

}
