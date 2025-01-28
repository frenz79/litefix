package com.litefix.models.fixmessage;

import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;

public class NopFixFieldValidator implements IFixFieldValidator {

	public static final NopFixFieldValidator DEFAULT = new NopFixFieldValidator();
	
	@Override
	public final boolean isValid(FieldType type, Object value) {
		return true;
	}

	@Override
	public String toString() {
		return "NopFixFieldValidator []";
	}

}
