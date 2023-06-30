package com.litefix.commons.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class ArrayUtils {

	public static void clearRegion(byte[] incoming, int start, int size) {
		Arrays.fill(incoming, start, start+size, (byte)0);
	}
	
	public static void moveToHead(byte[] arr, int start, int end) {
		if (arr.length <= 1) {
			return;
	    }
	    System.arraycopy(arr, start, arr, 0, end-start);
	}
	
	public static int toInteger( byte b ) {
		return Byte.toUnsignedInt(b) - 48;
	}
	
	public static int toInteger( ByteBuffer buff ) {
		return toInteger( buff.array(), buff.position(), buff.limit() );
	}
	
	public static int toInteger( byte[] arr, int start, int end ) {
		int int1 = toInteger( arr[start] );		
		switch(end-start) {
			case 1: return int1;
			case 2: return int1*10 + toInteger( arr[start+1] );
			case 3: return int1*100 + toInteger( arr[start+1] )*10 + toInteger( arr[start+2] );
			case 4: return int1*1000 + toInteger( arr[start+1] )*100 + toInteger( arr[start+2] )*10 + toInteger( arr[start+3] );
			default:
				int factor = 1;
				int ret = 0;
				for ( int i=end-1; i>=start; i-- ) {
					ret += toInteger(arr[i]) * factor;
					factor *= 10;
				}				
				return ret;
		}
	}
	
	public static final int findByte(byte[] largeArray, byte toSearch, int startOffset ) {
		return findByte( largeArray, toSearch, startOffset, largeArray.length);
	}
	
	public static final int findByte(byte[] largeArray, byte toSearch, int startOffset, int endOffset ) {
		for (int i = startOffset; i <= endOffset; i+=2) {
			if (largeArray[i]==toSearch ) {
				return i;
			}
			if (largeArray[i+1]==toSearch ) {
				return i+1;
			}
		}
		return -1;
	}
	
	public static final int findArray(byte[] value, byte[] str) {
		return findArray(value, str, 0, -1);
	}
	
	public static final int findArray4(byte[] value, byte[] str) {
		return findArray(value, str, 0, -1);
	}
	
	public static final int findArray(byte[] value, byte[] str, int startOffset) {
		return findArray(value, str, startOffset, -1);
	}
	
	public static final int findArray(byte[] value, byte[] str, int startOffset, int endOffset) {
		int valueCount = (endOffset>=0)?endOffset:value.length;
		int strCount = str.length;
		int fromIndex = startOffset;
		
	    if (strCount == 0) {
	        return -1;
	    }

        byte first = str[0];
        int max = (valueCount - strCount);
        for (int i = fromIndex; i <= max; i++) {
            // Look for first character.
            if (value[i] != first) {
                while (++i <= max && value[i] != first);
            }
            // Found first character, now look at the rest of value
            if (i <= max) {
                int j = i + 1;
                int end = j + strCount - 1;
                for (int k = 1; j < end && value[j] == str[k]; j++, k++);
                if (j == end) {
                    // Found whole string.
                    return i;
                }
            }
        }
        return -1;
	}
	
	public static final int findArray4(byte[] value, byte[] str, int startOffset, int endOffset) {
		int valueCount = (endOffset>=0)?endOffset:value.length;
		int strCount = str.length;
		int fromIndex = startOffset;
		
	    if (strCount == 0) {
	        return -1;
	    }

        byte first = str[0];
        byte second = str[1];
        byte third = str[2];
        byte fourth = str[3];
        
        int max = (valueCount - strCount);
        for (int i = fromIndex; i <= max; i++) {
            // Look for first character.
            if (value[i] != first || value[i+1] != second || value[i+2] != third || value[i+3] != fourth ) {
                while (++i <= max && ( value[i] != first || value[i+1] != second || value[i+2] != third || value[i+3] != fourth));
            }
            // Found first character, now look at the rest of value
            if (i <= max) {
                int j = i + 4;
                int end = j + strCount - 1;
                for (int k = 4; j < end-3 && value[j] == str[k]; j++, k++);
                if (j+3 == end) {
                    // Found whole string.
                    return i;
                }
            }
        }
        return -1;
	}
	
	
	/*
	public static void main( String arg[] ) {
		byte [] large = new String("sajffh89742389hfshr27fhkjhui23gyur237642298ahsfh'346379uiffh827ifhajkfhfh289yr18tdafuifgtt783ry8ohfuhwe798f8wefuwe78fy89we89fw").getBytes();
		byte [][] search = new byte[10024][];
		Random rnd = new Random();
		
		byte [] found = "2389hfshr".getBytes();
		
		for ( int i=0; i<search.length; i++ ) {
			search[i] = new byte[8];
			rnd.nextBytes( search[i] );
		}
		
		long start = System.nanoTime();
		for ( int i=0; i<search.length; i++ ) {
			int pos = findArray4( large, found );
		}
		long end = System.nanoTime();
		System.out.println("pos = "+(end-start)); // 728900
		
		int pos4 = findArray4( large, src, 0, large.length );
		
		System.out.println("pos = "+pos);
		System.out.println("pos4 = "+pos4);
		
	}*/
}
