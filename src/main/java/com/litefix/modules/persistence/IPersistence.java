package com.litefix.modules.persistence;

import java.io.Serializable;
import java.util.List;

public interface IPersistence<T extends Serializable> {

	int getAndIncrementOutgoingSeq();

	void purgeOutgoingMessage(int sequence);
	
	void storeOutgoingMessage(int sequence, T message);

	List<T> getAllOutgoingMessagesInRange(int valueAsInt, int valueAsInt2);

	T findOutgoingMessageBySeq(int i);

	int getLastOutgoingSeq();

	void reset();

	int getLastIncomingSeq();
	
	void setLastIncomingSeq( int seq );
	
	int incLastIncomingSeq();
	
	void close();
}
