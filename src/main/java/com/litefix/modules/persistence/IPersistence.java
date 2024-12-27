package com.litefix.modules.persistence;

import java.util.List;

import com.litefix.modules.Cloneable;

public interface IPersistence<T extends com.litefix.modules.Cloneable> {

	int getAndIncrementOutgoingSeq();

	void storeOutgoingMessage(int sequence, T message);

	List<T> getAllOutgoingMessagesInRange(int valueAsInt, int valueAsInt2);

	T findOutgoingMessageBySeq(int i);

	int getLastOutgoingSeq();

	void reset();

	int getLastIncomingSeq();
	int setLastIncomingSeq( int seq );
	
	void close();
}
