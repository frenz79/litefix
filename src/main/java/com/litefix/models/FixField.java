package com.litefix.models;

import java.nio.ByteBuffer;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.utils.ArrayUtils;

public class FixField implements Comparable<Object>{

	private static final int INT_SIZE_BYTES = Integer.SIZE/8;
	public static final int CHAR_SIZE_BYTES = Character.SIZE/8;
	private static final int LONG_SIZE_BYTES = Long.SIZE/8;
	private static final int DOUBLE_SIZE_BYTES = Double.SIZE/8;
	private static final int FLOAT_SIZE_BYTES = Float.SIZE/8;
	
	private ByteBuffer buffer;
	private int startTag;
	private int endTag;
	private int startVal;
	private int endVal;
	private int startBuff;
	private int endBuff;
	private int tag;
	
	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public FixField(){
		
	}
	
	public FixField( FixTag tag, String v ){
		this( tag.getTagNum(), v);		
	}
	
	public FixField( int tag, String v ){
		this( NumbersCache.toString(tag), v);		
	}
	
	public FixField( String tag, String v ){
		byte[] tagBytes = tag.getBytes();
		byte[] valBytes = v.getBytes();
		
		int buffSize = 	
				tagBytes.length
			+	CHAR_SIZE_BYTES
			+	valBytes.length;
				
		this.buffer = ByteBuffer.allocate( buffSize );
		
		this.startBuff = 0;
		this.endBuff = buffSize;
		
		this.startVal = tagBytes.length + CHAR_SIZE_BYTES;
		this.endVal = buffSize;
		
		this.startTag = 0;
		this.endTag = tagBytes.length;
		
		this.tag = NumbersCache.fromString(tag);
		
		this.buffer.put(tagBytes);
		this.buffer.putChar('=');
		this.buffer.put(valBytes);
	}
		
	public static FixField fromBuffer(FixTag tag, FixField field, ByteBuffer buffer, int startTag, int endTag, int startBuff, int endBuff, int startVal, int endVal) {
		field.buffer = buffer;
		field.startBuff = startBuff;
		field.endBuff = endBuff;
		field.startVal = startVal;
		field.endVal = endVal;
		field.startTag = startTag;
		field.endTag = endTag;
		field.tag = tag.getTagNum();
		return field;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public boolean contains(String v) {
		return v.length()==0 
			|| ArrayUtils.findArray(buffer.array(), v.getBytes(), startVal, endVal)>=0;
	}
	
	public boolean is(String v) {
		return v.length()==endVal-startVal 
			  && ArrayUtils.findArray(buffer.array(), v.getBytes(), startVal, endVal)>=0;
	}
	
	public boolean is(Character v) {
		return is(v.charValue());
	}
	
	public boolean is(float v) {
		// return is(String.valueOf(v));
		byte[] bytes = ByteBuffer.allocate(FLOAT_SIZE_BYTES).putFloat(v).array();		
		return ArrayUtils.findArray(buffer.array(), bytes, startVal, endVal)>=0;
	}
	
	public boolean is(long v) {
		// return is(String.valueOf(v));
		byte[] bytes = ByteBuffer.allocate(LONG_SIZE_BYTES).putLong(v).array();		
		return ArrayUtils.findArray(buffer.array(), bytes, startVal, endVal)>=0;
	}
	
	public boolean is(double v) {
		// return is(String.valueOf(v));
		byte[] bytes = ByteBuffer.allocate(DOUBLE_SIZE_BYTES).putDouble(v).array();		
		return ArrayUtils.findArray(buffer.array(), bytes, startVal, endVal)>=0;
	}
	
	public boolean is(int v) {
		// return is(String.valueOf(v));
		byte[] bytes = ByteBuffer.allocate(INT_SIZE_BYTES).putInt(v).array();		
		return ArrayUtils.findArray(buffer.array(), bytes, startVal, endVal)>=0;
	}
	
	public boolean is(char v) {
		// return is(String.valueOf(v));
		byte[] bytes = ByteBuffer.allocate(CHAR_SIZE_BYTES).putChar(v).array();		
		return ArrayUtils.findArray(buffer.array(), bytes, startVal, endVal)>=0;
	}
	
	public boolean is(byte[] v) {
		return ArrayUtils.findArray(buffer.array(), v, startVal, endVal)>=0;
	}
	
	public boolean is(ByteBuffer v) {
		return ArrayUtils.findArray(buffer.array(), v.array(), startVal, endVal)>=0;
	}
	
	public String valueAsString() {
		return new String( buffer.array(), startVal, endVal-startVal);
	}
	
	public int valueAsInt() {
		String val = valueAsString();
		return NumbersCache.fromString(val);
	}
	
	@Override
	public String toString() {
		return new String( buffer.array(), startTag, endVal-startTag);
	}
	
	public boolean isEmpty() {
		return startVal==endVal;
	}

	@Override
	public int compareTo(Object o) {
		if ( o instanceof FixField ) {
			FixField that = ((FixField)o);
			if ( getTag()!=that.getTag() ) {
				throw new RuntimeException("Cannot compare FixField with different tag");
			}
			int thisLen = this.getEndVal()-this.getStartVal();
			int thatLen = that.getEndVal()-that.getStartVal();
			
			if ( thisLen<thatLen ) return -1;
			if ( thisLen>thatLen ) return 1;
			byte[] thisBuff = this.buffer.array();
			byte[] thatBuff = that.buffer.array();
			
			for ( int i=0; i<thisLen; i++ ) {
				int delta = thisBuff[i+this.getStartVal()] - thatBuff[i+that.getStartVal()];
				if ( delta<0 ) return -1;
				if ( delta>0 ) return 1;
			}
			return 0;
		}
		return -1;
	}
	
	public int getStartTag() {
		return startTag;
	}

	public void setStartTag(int startTag) {
		this.startTag = startTag;
	}

	public int getEndTag() {
		return endTag;
	}

	public void setEndTag(int endTag) {
		this.endTag = endTag;
	}

	public int getStartVal() {
		return startVal;
	}

	public void setStartVal(int startVal) {
		this.startVal = startVal;
	}

	public int getEndVal() {
		return endVal;
	}

	public void setEndVal(int endVal) {
		this.endVal = endVal;
	}

	public int getStartBuff() {
		return startBuff;
	}

	public void setStartBuff(int startBuff) {
		this.startBuff = startBuff;
	}

	public int getEndBuff() {
		return endBuff;
	}

	public void setEndBuff(int endBuff) {
		this.endBuff = endBuff;
	}
}
