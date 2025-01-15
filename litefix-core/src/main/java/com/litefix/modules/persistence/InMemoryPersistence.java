package com.litefix.modules.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPersistence<T extends Serializable> implements IPersistence<T>{

	private final Map<Integer,T> messages = new HashMap<>();
	
	final AtomicInteger incomingSeqNum = new AtomicInteger(0);
	final AtomicInteger outgoingSeqNum = new AtomicInteger(1);
	
	@Override
	public void resetOutgoingSequence() {
		outgoingSeqNum.set(1);
	}
	
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
		this.messages.put(sequence, message);
	}

	@Override
	public List<T> getAllOutgoingMessagesInRange(int start, int end) {
		List<T> ret = null;
		if ( end==0 ) {
			end = getLastOutgoingSeq()-1;
		}
		
		ret = new ArrayList<>(end-start);
		for ( int i=start; i<=end; i++ ) {
			T msg = messages.get(i);
			if ( msg!=null ) {
				ret.add(msg);
			}
		}
		
		return ret;
	}

	@Override
	public T findOutgoingMessageBySeq(int i) {
		return this.messages.get(i);
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
