package com.litefix.models.fixmessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.utils.ByteUtils;
import com.litefix.commons.utils.Pair;
import com.litefix.commons.utils.TimeUtils;

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
	
	public GroupDecoder asGroupDecoder(int id) {
		return new GroupDecoder( id );
	}
	
	public class GroupDecoder {
		private int occurrence = 0;
		private int groupOffset = 0;
		private final int groupSize;
		
		GroupDecoder( int fieldId ){
			Pair<Integer, Integer> p = getTagValueAsInt(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), occurrence);
			this.groupSize = p.getLeft();
			this.groupOffset = p.getRight();
		}
		
		public GroupDecoder at(int i) {
			this.occurrence = i;
			return this;
		}
		
		public int getGroupSize() {
			return groupSize;
		}
		
		public BigDecimal asBigDecimal(int fieldId) {
			return new BigDecimal(
				getTagValue(msgBuff, msgBuffFrom+groupOffset, msgBuffLen-groupOffset, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), occurrence).getLeft());
		}

		public char asChar(int fieldId) {
			return getTagValue(msgBuff, msgBuffFrom+groupOffset, msgBuffLen-groupOffset, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), occurrence).getLeft().charAt(0);
		}
	}
	
	public boolean isEqual( int fieldId, byte[] buffCompare ) {
		byte[] field = NumbersCache.toStringBytes(fieldId);
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
	
	public BigDecimal asBigDecimal(int i) {
		return new BigDecimal(asString(i));
	}
	
	public String asString( int fieldId ) {
		return getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft();
	}
	
	public Boolean asBoolean( int fieldId ) {
		return getTagValueAsBoolean(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft();
	}
	
	public Integer asInt( int fieldId ) {
		return getTagValueAsInt(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft();
	}

	public long asLong( int fieldId ) {
		return Long.valueOf(getTagValueAsInt(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft());
	}

	public double asFloat( int fieldId ) {
		return Double.valueOf(getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft());
	}
	
	public char asChar( int fieldId ) {
		return getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft().charAt(0);
	}
	
	public LocalDateTime asTimestamp( int fieldId ) {
		return TimeUtils.toUTCTimestamp(getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft());
	}
	
	public byte[] getBytes( int fieldId ) {
		return getTagValue(msgBuff, msgBuffFrom, msgBuffLen, fieldId, dictionary.getFieldSep(), dictionary.getDecimalSep(), 0).getLeft().getBytes();
	}
	
	private static final Pair<Boolean,Integer> getTagValueAsBoolean( byte[] buff, int startOffset, int len, int fieldId, char fieldSep, char decimalSep, int occurrence  ) {
		byte[] field = NumbersCache.toStringBytes(fieldId);
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
				if ( buff[endIdx]==fieldSep ) {
					return new Pair<>(null, endIdx);
				}
				if ( buff[endIdx]=='N') {
					return new Pair<>(Boolean.FALSE, endIdx);
				}
				if ( buff[endIdx]=='Y') {
					return new Pair<>(Boolean.TRUE, endIdx);
				}
				throw new RuntimeException(String.format("Invalid boolean value:%c", buff[endIdx]));

			}			
		}
		return new Pair<>(null, endOffset);
//		throw new RuntimeException(
//			String.format("Cannot find field:%d from offset:%d in message:%s", fieldId, startOffset, new String(ByteBuffer.wrap(buff,startOffset,len).array())));
	}
	
	private static final Pair<Integer,Integer> getTagValueAsInt( byte[] buff, int startOffset, int len, int fieldId, char fieldSep, char decimalSep, int occurrence ) {
		byte[] field = NumbersCache.toStringBytes(fieldId);
		int fieldLen = field.length;
		int endOffset = startOffset+len;	
		int occurrenceCount = 0;
		
		for (int i=startOffset; i<endOffset; i++) {
			int startIdx = 0;
			for (; startIdx<fieldLen; startIdx++) {
				if (field[startIdx]!=buff[i]) {
					break;
				}
				++i;
			}
			
			if (startIdx==fieldLen && buff[i]=='=') {
				if (occurrenceCount==occurrence) {
					int endIdx = ++i;
					startIdx = endIdx;
					
					for(; endIdx<endOffset; endIdx++) {
						if (buff[endIdx]==fieldSep) {
							break;
						}					
					}
					if (endIdx==startIdx) {
						return null;
					}
					return new Pair<>(ByteUtils.charsToInt( buff, startIdx, endIdx), endIdx );
				}
				occurrenceCount++;
			}			
		}
		return null;
	}
	
	private static final Pair<String,Integer> getTagValue( byte[] buff, int startOffset, int len, int fieldId, char fieldSep, char decimalSep, int occurrence ) {
		byte[] field = NumbersCache.toStringBytes(fieldId);
		int fieldLen = field.length;
		int endOffset = startOffset+len;
		int occurrenceCount = 0;
		
		for (int i=startOffset; i<endOffset; i++) {
			int startIdx = 0;
			for (; startIdx<fieldLen; startIdx++) {
				if (field[startIdx]!=buff[i]) {
					break;
				}
				++i;
			}
			
			if (startIdx==fieldLen && buff[i]=='=') {
				if (occurrenceCount==occurrence) {
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
						return new Pair<>("", endIdx);
					}
					/* bugged when contains .
					if (isNumber) {
						try {
							int num = ByteUtils.charsToInt( buff, startIdx, endIdx);
							return new Pair<>(NumbersCache.toString(num), endIdx);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					*/
					return new Pair<>(new String(buff, startIdx, endIdx-startIdx), endIdx);
				}
				
				occurrenceCount++;
			}			
		}	
		return new Pair<>("", endOffset);
	//	throw new RuntimeException(
	//		String.format("Cannot find field:%d from offset:%d in message:%s", fieldId, startOffset, new String(ByteBuffer.wrap(buff,startOffset,len).array())));
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
