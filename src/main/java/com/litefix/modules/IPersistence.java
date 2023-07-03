package com.litefix.modules;

import com.litefix.models.FixMessage;

public interface IPersistence {

	int getAndIncrementSeq();

	void store(int sequence, FixMessage message);

}
