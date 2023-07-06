package com.litefix.commons.utils;

public class ByteUtils {

	public static byte[] intToBytes(int value) {
	    return new byte[] { 
	        (byte)(value >> 24),
	        (byte)(value >> 16),
	        (byte)(value >> 8),
	        (byte)value };
	}
	
	public static int bytesToInt(byte[] bytes) {
	     return bytes[0] << 24 
	    	 | (bytes[1] & 0xFF) << 16 
	    	 | (bytes[2] & 0xFF) << 8 
	    	 | (bytes[3] & 0xFF);
	}
}
