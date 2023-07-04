package com.litefix.modules;

import java.util.List;

import com.litefix.models.FixMessage;

public interface IPersistence {

	int getAndIncrementOutgoingSeq();

	void storeOutgoingMessage(int sequence, FixMessage message);

	List<FixMessage> getAllOutgoingMessagesInRange(int valueAsInt, int valueAsInt2);

	FixMessage findOutgoingMessageBySeq(int i);

	int getLastOutgoingSeq();

	void reset();

	int getLastIncomingSeq();

}
