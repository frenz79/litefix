package com.litefix.models.fixmessage;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.utils.ByteUtils;

public class FixMessageDecoder extends AbstractEncoderDecoder {

	private final String msgType;
	private final int seqNum;
	private final byte[] msgBuff;
	private final int msgBuffFrom;
	private final int msgBuffLen;
	private final long rcvNanoTime;
	
	public static FixMessageDecoder newDecoder( FixMessageDictionary dictionary, byte[] msgBuff, long rcvNanoTime ) {
		return newDecoder(dictionary, msgBuff, 0, msgBuff.length, rcvNanoTime);
	}
	
	public static FixMessageDecoder newDecoder( FixMessageDictionary dictionary, byte[] msgBuff, int msgBuffFrom, int msgBuffLen, long rcvNanoTime ) {
		FixMessageDecoder decoder  = new FixMessageDecoder( msgBuff, msgBuffFrom, msgBuffLen, dictionary, rcvNanoTime ); 
		return decoder;
	}
	
	private FixMessageDecoder( byte[] msgBuff, FixMessageDictionary dictionary, long rcvNanoTime ) {
		this(msgBuff, 0, msgBuff.length, dictionary, rcvNanoTime);
	}
	
	private FixMessageDecoder( byte[] msgBuff, int msgBuffFrom, int msgBuffLen, FixMessageDictionary dictionary, long rcvNanoTime) {
		super(dictionary);
		this.msgBuff = msgBuff;
		this.msgBuffFrom = msgBuffFrom;	
		this.msgBuffLen = msgBuffLen;	
		this.msgType = asString( 35 );
		this.seqNum = asInt( 34 );
		this.rcvNanoTime = rcvNanoTime;
	}
	
	public boolean isEqual( int fieldId, byte[] buffCompare ) {
		byte[] field = NumbersCache.toBytes(fieldId);
		int fieldLen = field.length;
		int endOffset = msgBuffFrom+msgBuffLen;
		char fieldSep = dictionary.getFieldSep();
		
		for (int i=msgBuffFrom; i<endOffset; i++) {
			int startIdx = 0;
			for (; startIdx<fieldLen; startIdx++) {
				if (field[startIdx]!=msgBuff[i]) {
					break;
				}
				++i;
			}
			
			if (startIdx==fieldLen && msgBuff[i]=='=') {
				int endIdx = ++i;
				startIdx = endIdx;
				
				for(; endIdx<endOffset; endIdx++) {
					if (msgBuff[endIdx]==fieldSep) {
						break;
					}					
					
				}
				if (endIdx==startIdx) {
					return false;
				}
				
				return Arrays.compare(msgBuff, startIdx, endIdx, buffCompare, 0, buffCompare.length ) == 0;
			}			
		}		
		throw new RuntimeException(String.format("Cannot find field:%d", fieldId));
	}
	
	public String asString( int fieldId ) {
		return getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep());
	}
	
	public int asInt( int fieldId ) {
		return getTagValueAsInt(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep());
	}

	public long asLong( int fieldId ) {
		return Long.valueOf(getTagValueAsInt(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep()));
	}

	public double asFloat( int fieldId ) {
		return Double.valueOf(getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep()));
	}
	
	public char asChar( int fieldId ) {
		return getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep()).charAt(0);
	}
	
	public LocalDateTime asTimestamp( int fieldId ) {
		return toUTCTimestamp(getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep()));
	}
	
	public byte[] getBytes( int fieldId ) {
		return getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep()).getBytes();
	}
	
	private static final int getTagValueAsInt( byte[] buff, int startOffset, int len, int fieldId, char fieldSep, char decimalSep ) {
		byte[] field = NumbersCache.toBytes(fieldId);
		int fieldLen = field.length;
		int endOffset = startOffset+len;	
		
		for (int i=startOffset; i<endOffset; i++) {
			int startIdx = 0;
			for (; startIdx<fieldLen; startIdx++) {
				if (field[startIdx]!=buff[i]) {
					break;
				}
				++i;
			}
			
			if (startIdx==fieldLen && buff[i]=='=') {
				int endIdx = ++i;
				startIdx = endIdx;
				
				for(; endIdx<endOffset; endIdx++) {
					if (buff[endIdx]==fieldSep) {
						break;
					}					
				}
				if (endIdx==startIdx) {
					return 0;
				}
				return ByteUtils.charsToInt( buff, startIdx, endIdx);
			}			
		}		
		throw new RuntimeException(
			String.format("Cannot find field:%d from offset:%d in message:%s", fieldId, startOffset, new String(ByteBuffer.wrap(buff,startOffset,len).array())));
	}
	
	private static final String getTagValue( byte[] buff, int startOffset, int len, int fieldId, char fieldSep, char decimalSep ) {
		byte[] field = NumbersCache.toBytes(fieldId);
		int fieldLen = field.length;
		int endOffset = startOffset+len;
		
		for (int i=startOffset; i<endOffset; i++) {
			int startIdx = 0;
			for (; startIdx<fieldLen; startIdx++) {
				if (field[startIdx]!=buff[i]) {
					break;
				}
				++i;
			}
			
			if (startIdx==fieldLen && buff[i]=='=') {
				int endIdx = ++i;
				startIdx = endIdx;
				boolean isNumber = true;
				
				for(; endIdx<endOffset; endIdx++) {
					if (buff[endIdx]==fieldSep) {
						break;
					}					
					if ( isNumber ) {
						isNumber = (buff[endIdx]>='0' && buff[endIdx]<='9' || buff[endIdx]==decimalSep);
					}
				}
				if (endIdx==startIdx) {
					return "";
				}
				if (isNumber) {
					try {
						int num = ByteUtils.charsToInt( buff, startIdx, endIdx);
						return NumbersCache.toString(num);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return new String(buff, startIdx, endIdx-startIdx);
			}			
		}		
		throw new RuntimeException(
			String.format("Cannot find field:%d from offset:%d in message:%s", fieldId, startOffset, new String(ByteBuffer.wrap(buff,startOffset,len).array())));
	}
	
	

	public String getMsgType() {
		return msgType;
	}

	public byte[] getMsgBuff() {
		return msgBuff;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public int getMsgBuffFrom() {
		return msgBuffFrom;
	}

	public int getMsgBuffLen() {
		return msgBuffLen;
	}

	public long getRcvNanoTime() {
		return rcvNanoTime;
	}

	@Override
	public String toString() {
		return "FixMessageDecoder [msgType=" + msgType + ", seqNum=" + seqNum + ", rcvNanoTime=" + rcvNanoTime 
				+ ", msgBuff=" + Arrays.toString(msgBuff)+ "]";
	}
}
