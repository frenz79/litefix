package com.litefix.models.fixmessage;

import java.util.regex.Pattern;

import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;

public class RegExpFixFieldValidator implements IFixFieldValidator {

	private final Pattern pattern;
	
	public RegExpFixFieldValidator( String regExp ) {
		this.pattern = Pattern.compile(regExp);
	}
	
	@Override
	public boolean isValid(FieldType type, Object value) {
		return this.pattern.matcher((String)value).matches();
	}

	@Override
	public String toString() {
		return "RegExpFixFieldValidator [pattern=" + pattern + "]";
	}

}
