package com.litefix.modules;

import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.FixMessage;

public interface IFixMessageValidator {

	public boolean validate( FixMessage msg ) throws SessionRejectMessageException;
}
