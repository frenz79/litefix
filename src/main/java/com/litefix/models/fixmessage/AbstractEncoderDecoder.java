package com.litefix.models.fixmessage;

import java.io.Serializable;

public abstract class AbstractEncoderDecoder implements Serializable {

	final FixMessageDictionary dictionary;

	public AbstractEncoderDecoder(FixMessageDictionary dictionary) {
		super();
		this.dictionary = dictionary;
	}

	public FixMessageDictionary getDictionary() {
		return dictionary;
	}

}
