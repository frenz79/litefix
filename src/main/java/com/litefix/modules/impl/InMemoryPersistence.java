package com.litefix.modules.impl;

import java.util.List;

import com.litefix.commons.collections.IndexedFifoQueue;
import com.litefix.models.FixMessage;
import com.litefix.modules.IPersistence;

public class InMemoryPersistence implements IPersistence{
	private final String beginString, senderCompId, targetCompId;
	private int incomingSeq = 0;
	private int outgoingSeq = 0;
	private IndexedFifoQueue<FixMessage> sentMessages = new IndexedFifoQueue<>(64000);
	
	public InMemoryPersistence(String beginString, String senderCompId, String targetCompId) {
		this.beginString = beginString;
		this.senderCompId = senderCompId;
		this.targetCompId = targetCompId;
	}
	
	@Override
	public int getLastOutgoingSeq() {
		return outgoingSeq;
	}
	
	@Override
	public int getAndIncrementOutgoingSeq() {
		return ++outgoingSeq;
	}

	@Override
	public void storeOutgoingMessage(int sequence, FixMessage message) {
		this.sentMessages.put(sequence, message.clone());
	}

	@Override
	public List<FixMessage> getAllOutgoingMessagesInRange(int beginSeq, int endSeq) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public FixMessage findOutgoingMessageBySeq(int i) {
		return sentMessages.get( i );
	}
	
	@Override
	public void reset() {
		this.outgoingSeq = 0;
		this.sentMessages.clear();
	}

	@Override
	public int getLastIncomingSeq() {
		return incomingSeq;
	}

}
