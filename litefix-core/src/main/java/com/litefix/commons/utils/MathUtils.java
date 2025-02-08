package com.litefix.commons.utils;

import java.math.BigDecimal;

public class MathUtils {

	public static final long SCALE_FACTOR = BigDecimal.valueOf(10l).pow(10).longValue();
	
	public static long[] POW10 = new long[] {
		(long)Math.pow(10,0),
		(long)Math.pow(10,1),
		(long)Math.pow(10,2),
		(long)Math.pow(10,3),
		(long)Math.pow(10,4),
		(long)Math.pow(10,5),
		(long)Math.pow(10,6),
		(long)Math.pow(10,7),
		(long)Math.pow(10,8),
		(long)Math.pow(10,9),
		(long)Math.pow(10,10),
			
	};
	
	public static int charToInt( char c ) {
		return c - '0';
	}
	
	public static long toUnscaledNumber( String n ) {
		int decimalSepIdx = -1;
		int firstDecimalIdx = -1;
		for (int i=n.length()-1; i>=0; i--) {
			if ( n.charAt(i)=='.' ) {
				decimalSepIdx = i;
				break;
			} else if ( firstDecimalIdx<0 && n.charAt(i)!='0') {
				firstDecimalIdx = i;
			}
		}
		
		if ( decimalSepIdx<0 ) {
			return Long.valueOf(n).longValue() * SCALE_FACTOR;
		}
		if ( firstDecimalIdx<0 ) {
			return Long.valueOf(n.substring(0,decimalSepIdx)).longValue() * SCALE_FACTOR;
		}
		
		int decimals = firstDecimalIdx - decimalSepIdx;
		
		long ret = 0l;
		int pos = 0;
		
		for (int i=decimalSepIdx+1; i<n.length(); i++) {
			ret = ret + charToInt(n.charAt(i)) * POW10[decimals-(pos+1)];
			pos++;
			if ( decimals==pos ) {
				break;
			}
		}
		
		pos = 0;
		for (int i=decimalSepIdx-1; i>=0; i--) {
			ret = ret + charToInt(n.charAt(i)) * POW10[decimals+pos];
			pos++;
		}
		
		return ret * (long)Math.pow(10, 10-decimals);
	}
	
	public static String toScaledString( long n ) {
		StringBuilder ret = new StringBuilder( String.valueOf(n) );
		
		if (ret.length()<10) {
			StringBuilder prefix = new StringBuilder("0.");
			for (int i=0; i<10-ret.length(); i++) {
				prefix.append('0');
			}
			ret = prefix.append(ret);
			return ret.toString();
		}

		return ret.substring(0, ret.length()-10)
			   + "." +
			   ret.substring(ret.length()-10);
	}
	
	public static int calcFixChecksum( final StringBuilder strBld ) {
		int checksum = 0;
		for ( int i=0; i<strBld.length(); i++) {
			checksum += strBld.charAt(i);
		}
		checksum  &= 0xFF; // better than sum % 256 since it avoids overflow issues
		return checksum;
	}
	
	public static final int calcFixChecksum( byte[] target, int start, int end ) {	
		int checksum = 0;
		// it's a bit faster decrementing
		--end;
		for ( int i=end; i>=start; --i) {
			checksum += target[i];
		}
		checksum  &= 0xFF; // better than sum % 256 since it avoids overflow issues
		return checksum;
	}
	
	public static final int getDigits( int number ) {
		if (number < 100_000) {
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
		    if (number < 10_000_000) {
		        if (number < 1000000) {
		            return 6;
		        } else {
		            return 7;
		        }
		    } else {
		        if (number < 100_000_000) {
		            return 8;
		        } else {
		            if (number < 1_000_000_000) {
		                return 9;
		            } else {
		                return 10;
		            }
		        }
		    }
		}
	}
}
