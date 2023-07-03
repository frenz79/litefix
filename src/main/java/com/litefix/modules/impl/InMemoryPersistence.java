package com.litefix.modules.impl;

import java.util.HashMap;
import java.util.Map;

import com.litefix.models.FixMessage;
import com.litefix.modules.IPersistence;

public class InMemoryPersistence implements IPersistence{
	private final String beginString, senderCompId, targetCompId;

	private int outgoingSeq = 0;
	private Map<Integer,FixMessage> sentMessages = new HashMap<>(64000);
	
	public InMemoryPersistence(String beginString, String senderCompId, String targetCompId) {
		this.beginString = beginString;
		this.senderCompId = senderCompId;
		this.targetCompId = targetCompId;
	}
	
	@Override
	public int getAndIncrementSeq() {
		return ++outgoingSeq;
	}

	@Override
	public void store(int sequence, FixMessage message) {
		this.sentMessages.put(sequence, message);
	}

}
