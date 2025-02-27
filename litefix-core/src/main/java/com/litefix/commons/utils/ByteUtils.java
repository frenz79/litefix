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
	
	private static final byte BYTE_0 = (byte)'0';
	private static final byte BYTE_9 = (byte)'9';
	
	public static final int charsToInt( byte h, byte d, byte u ) {
		return (h - BYTE_0)*100 + (d - BYTE_0)*10 + (u - BYTE_0);
	}
	
	public static final int charsToInt( byte d, byte u ) {
		return (d - BYTE_0)*10 + (u - BYTE_0);
	}
	
	public static final int charsToInt( byte c[], int begin, int end ) {
	    int total=0, x=0, m=1;
	    for(int i=end-1;i>=begin;i--){
	        x = c[i] - BYTE_0;
	        x = x*m;
	        m *= 10;
	        total += x;
	    }	    
	    return total;
	}
}
