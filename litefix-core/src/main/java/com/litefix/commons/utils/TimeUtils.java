package com.litefix.commons.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
	
	public static final DateTimeFormatter UTC_TIMESTAMP_SEC = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss");
	public static final DateTimeFormatter UTC_TIMESTAMP_MILLIS = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
	
	public static String getSendingTime() {
		return LocalDateTime.now(ZoneOffset.UTC).format( UTC_TIMESTAMP_MILLIS );
	}
	
	public static LocalDateTime fromSendingTime( String sendingTime ) {
		return LocalDateTime.parse(sendingTime, UTC_TIMESTAMP_MILLIS);
	}
	
	// YYYYMMDD-HH:MM:SS.sss
	public static LocalDateTime toUTCTimestamp( String val ) {
		DateTimeFormatter formatter = (val.charAt(val.length()-4)=='.')?UTC_TIMESTAMP_MILLIS:UTC_TIMESTAMP_SEC;
		return LocalDateTime.parse(val, formatter);
	}
}
