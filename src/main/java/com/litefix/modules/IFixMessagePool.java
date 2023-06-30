package com.litefix.modules;

import com.litefix.models.FixMessage;

public interface IFixMessagePool {

	FixMessage get();

	void release(FixMessage message);

}
