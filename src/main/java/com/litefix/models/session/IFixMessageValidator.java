package com.litefix.models.session;

import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;

public interface IFixMessageValidator {

	boolean isValidMessage(FixMessageDecoder decoder, String msgType) throws SessionRejectMessageException;

	boolean isGarbled(byte[] buffer, int from, int len);

	boolean isAdministrativeMessage(String msgType);

}