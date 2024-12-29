package com.litefix.modules.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPersistence<T extends Serializable> implements IPersistence<T>{

	private final List<T> messages = new ArrayList<>();
	
	final AtomicInteger incomingSeqNum = new AtomicInteger(0);
	final AtomicInteger outgoingSeqNum = new AtomicInteger(1);
	
	@Override
	public int getAndIncrementOutgoingSeq() {
		return outgoingSeqNum.getAndIncrement();
	}

	@Override
	public void purgeOutgoingMessage(int sequence) {
		outgoingSeqNum.set(1);
	}
	
	@Override
	public int getLastOutgoingSeq() {
		return outgoingSeqNum.get();
	}

	@Override
	public int getLastIncomingSeq() {
		return incomingSeqNum.get();
	}

	@Override
	public void setLastIncomingSeq(int seq) {
		incomingSeqNum.set(seq);
	}
	
	@Override
	public int incLastIncomingSeq() {
		return incomingSeqNum.incrementAndGet();
	}	
	
	@Override
	public void storeOutgoingMessage(int sequence, T message) {
		this.messages.add(sequence-1, message);
	}

	@Override
	public List<T> getAllOutgoingMessagesInRange(int valueAsInt, int valueAsInt2) {
		return this.messages;
	}

	@Override
	public T findOutgoingMessageBySeq(int i) {
		return this.messages.get(i-1);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub	
	}
}
