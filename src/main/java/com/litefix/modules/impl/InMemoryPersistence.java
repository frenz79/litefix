package com.litefix.modules.impl;

import java.util.List;

import com.litefix.commons.collections.IndexedFifoQueue;
import com.litefix.modules.IPersistence;

public class InMemoryPersistence<T extends com.litefix.modules.Cloneable> implements IPersistence<T>{
	private final String beginString, senderCompId, targetCompId;
	private int incomingSeq = 0;
	private int outgoingSeq = 0;
	private IndexedFifoQueue<T> sentMessages = new IndexedFifoQueue<>(64000);
	
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
	public void storeOutgoingMessage(int sequence, T message) {
		this.sentMessages.put(sequence, (T)message.clone());
	}

	@Override
	public List<T> getAllOutgoingMessagesInRange(int beginSeq, int endSeq) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public T findOutgoingMessageBySeq(int i) {
		return sentMessages.get( i );
	}
	
	@Override
	public void reset() {
		this.outgoingSeq = 0;
		this.sentMessages.clear();
	}

	@Override
	public int getLastIncomingSeq() {
		return this.incomingSeq;
	}
	
	@Override
	public int setLastIncomingSeq( int seq ) {
		this.incomingSeq = seq;
		return this.incomingSeq;
	}

	@Override
	public void close() {
		reset();
	}
}
