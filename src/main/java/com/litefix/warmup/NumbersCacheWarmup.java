package com.litefix.warmup;

import com.litefix.caches.NumbersCache;

public class NumbersCacheWarmup {

	public static void warmup( int counter ) {		
		for( int i=0; i<counter; i++ ) {
			for ( int j=0; j<NumbersCache.CACHE_SIZE; j++ ) {
				NumbersCache.toPaddedString( j );
				NumbersCache.toString( j );
			}
		}
	}
}
