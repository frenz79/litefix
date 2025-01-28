package com.litefix.models.fixmessage;

import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;

public interface IFixFieldValidator {

	public boolean isValid( FieldType type, Object value );
}
