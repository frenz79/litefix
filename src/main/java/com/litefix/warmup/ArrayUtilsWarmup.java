package com.litefix.warmup;

import com.litefix.commons.utils.ArrayUtils;

public class ArrayUtilsWarmup {

	public static void warmup( int counter ) {	
		for( int i=0; i<counter; i++ ) {			
			for ( int j=0; j<1000; j++ ) {
				byte [] buff = String.valueOf(j).getBytes();
				ArrayUtils.toInteger( buff, 0, buff.length );
			}			
		}
		
		byte[] largeBuff = new String("1234568978hgsdiòvnsdiovh9w85672903u3rjfsdkhc89753409ijowpjvf3450834kpdjv").getBytes();
		
		for( int i=0; i<counter; i++ ) {
			for ( int j=0; j<1000; j++ ) {
				byte [] buff = String.valueOf(j).getBytes();
				ArrayUtils.findArray( largeBuff, buff );
				ArrayUtils.findByte(largeBuff,  String.valueOf(j).getBytes()[0], 0);
			}			
		}
	}	
}
