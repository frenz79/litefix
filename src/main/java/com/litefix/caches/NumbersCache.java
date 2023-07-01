package com.litefix.caches;

import java.util.HashMap;
import java.util.Map;

public class NumbersCache {

	public static final int CACHE_SIZE = 50000;
	
	private static String[] int2str = new String[CACHE_SIZE];
	private static String[] int2PaddedStr = new String[CACHE_SIZE];
	private static Map<String,Integer>  str2int = new HashMap<>( CACHE_SIZE * 2);
	
	static {
		for (int i=0; i<CACHE_SIZE; i++) {
			int2str[i] = String.valueOf( i );

			String str = String.valueOf( i );
			int2PaddedStr[i] = (i<10)?"00"+str:(i<100)?"0"+str:str;

			str2int.put(int2str[i], i);
		}
	}
	
	public static String toString( int v ) {
		return (v<CACHE_SIZE)?int2str[v]:String.valueOf(v);
	}
	
	public static String toPaddedString( int v ) {
		return (v<CACHE_SIZE)?int2PaddedStr[v]:String.valueOf(v);
	}
	
	public static int fromString( String v ) {
		Integer r = str2int.get(v);
		return (r!=null)?r:Integer.valueOf( v );
	}
}
