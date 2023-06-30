package com.litefix.commons.utils;

public class MathUtils {

	public static final int calcChecksum( byte[] target, int start, int end ) {		
		int checksum = 0;

		for ( int i=start; i<end; i++) {
			checksum += target[i];
		}
		checksum  &= 0xFF; // better than sum % 256 since it avoids overflow issues
		return checksum;
	}
	
	public static final int getDigits( int number ) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 1;
		        } else {
		            return 2;
		        }
		    } else {
		        if (number < 1000) {
		            return 3;
		        } else {
		            if (number < 10000) {
		                return 4;
		            } else {
		                return 5;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 6;
		        } else {
		            return 7;
		        }
		    } else {
		        if (number < 100000000) {
		            return 8;
		        } else {
		            if (number < 1000000000) {
		                return 9;
		            } else {
		                return 10;
		            }
		        }
		    }
		}
	}
}
