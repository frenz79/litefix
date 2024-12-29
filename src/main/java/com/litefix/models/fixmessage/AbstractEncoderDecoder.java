package com.litefix.models.fixmessage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractEncoderDecoder implements Serializable {

	public static final DateTimeFormatter UTC_TIMESTAMP_SEC = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss");
	public static final DateTimeFormatter UTC_TIMESTAMP_MILLIS = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

	final FixMessageDictionary dictionary;

	public AbstractEncoderDecoder(FixMessageDictionary dictionary) {
		super();
		this.dictionary = dictionary;
	}

	public FixMessageDictionary getDictionary() {
		return dictionary;
	}
	
	
	int calcChecksum( final StringBuilder strBld ) {
		int checksum = 0;
		for ( int i=0; i<strBld.length(); i++) {
			checksum += strBld.charAt(i);
		}
		checksum  &= 0xFF; // better than sum % 256 since it avoids overflow issues
		return checksum;
	}
	
	// YYYYMMDD-HH:MM:SS.sss
		LocalDateTime toUTCTimestamp( String val ) {
			DateTimeFormatter formatter = (val.charAt(val.length()-4)=='.')?UTC_TIMESTAMP_MILLIS:UTC_TIMESTAMP_SEC;
			return LocalDateTime.parse(val, formatter);
		}
	
}
