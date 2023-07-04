package com.litefix.commons.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
	
	// 20221216-12:21:31.683
	private static final DateTimeFormatter SENDING_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
	
	public static String getSendingTime() {
		return LocalDateTime.now(ZoneOffset.UTC).format( SENDING_TIME_FORMATTER );
	}
}
