package com.litefix.modules.impl;

import java.util.UUID;

import com.litefix.commons.collections.LockFreeStack;
import com.litefix.models.FixMessage;
import com.litefix.modules.IFixMessagePool;

public class DefaultFixMessagePool implements IFixMessagePool {
	
	private static final String signatureId = UUID.randomUUID().toString();
	private final LockFreeStack<FixMessage> objectPool = new LockFreeStack<>();
		
	public DefaultFixMessagePool() {
		for ( int i=0; i<4; i++ ) {
			objectPool.push( new FixMessage(signatureId+"@"+i) );
		}
	}
	
	@Override
	public FixMessage get() {
		FixMessage m = objectPool.pop().reset();
		return m;
	}

	@Override
	public void release(FixMessage message) {
		if ( !message.getPoolId().startsWith(signatureId) ) {
			throw new RuntimeException("Cannot release an unknown object");
		}
		objectPool.push(message);
	}
}
