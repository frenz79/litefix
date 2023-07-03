package com.litefix.modules;

import java.util.List;

import com.litefix.models.FixMessage;

public interface IPersistence {

	int getAndIncrementSeq();

	void store(int sequence, FixMessage message);

	List<FixMessage> getAllMessagesInRange(int valueAsInt, int valueAsInt2);

	FixMessage findMessageBySeq(int i);

	int getLastSeq();

}
