package com.litefix.models;

import java.nio.ByteBuffer;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.IFixConst;
import com.litefix.commons.interfaces.IPoolable;
import com.litefix.commons.utils.ArrayUtils;
import com.litefix.commons.utils.MathUtils;


/**
 * 
 *  One single big buffer:
 *  
 *       header << | >> body
 *  [______________|_________________]
 *  
 *  header is appended right to left, body left to right
 *  
 */
public class FixMessage implements IPoolable {
	
	public static final int DEFAULT_BUFF_SIZE = 1024 * 16;
	public static final int DEFAULT_BUFF_MID  = 128;
	
	private final Metrics metrics;
	private ByteBuffer buffer;
	private byte[]     bufferArray;
	
	private int bodyLen = 0;
	private int headerLen = 0;
	private int headerStart = DEFAULT_BUFF_MID;
	private int bodyStart = DEFAULT_BUFF_MID;
	
	private MsgType msgType = null;

	private final String poolId;
	private Status    poolStatus;
	
	public FixMessage( String poolId ) {
		this.metrics = new Metrics();
		this.buffer = ByteBuffer.wrap( new byte[DEFAULT_BUFF_SIZE] ) ;//.allocate( DEFAULT_BUFF_SIZE );
		this.bufferArray = buffer.array();
		this.poolId = poolId;
		this.poolStatus = Status.FREE;
	}
	
	@Override
	public FixMessage reset() {
		this.bodyLen = 0;
		this.headerLen = 0;
		this.headerStart = DEFAULT_BUFF_MID;
		this.bodyStart = DEFAULT_BUFF_MID;
		this.msgType = null;
		this.buffer.rewind();
		return this;
	}
	
	public FixMessage setMsgType(String msgType) {
		this.msgType = MsgType.buildFrom(msgType.getBytes(), 0, msgType.getBytes().length);
		return this;
	}
	
	public static class Metrics {
		public long startRcvTime;
		public long endRcvTime;
	}

	public Metrics getMetrics() {
		return metrics;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public MsgType getMsgType() {
		if ( msgType==null ) {
			int startPos = ArrayUtils.findArray( bufferArray, IFixConst.StandardHeader.MsgType.getTagBytes() );
			startPos += IFixConst.StandardHeader.MsgType.getTagBytes().length;			
			int endPos = ArrayUtils.findByte( bufferArray, IFixConst.FIELD_SEPARATOR, startPos );
			msgType = MsgType.buildFrom(bufferArray, startPos, endPos);
		}
		return msgType;
	}	
	
	public boolean is (FixTag fixTag, byte[] val) {
		int startPos = ArrayUtils.findArray( bufferArray, fixTag.getTagBytes() );
		if ( startPos<0 ) {
			return false;
		}
		startPos += fixTag.getTagBytes().length;
		int endPos = ArrayUtils.findByte( bufferArray, IFixConst.FIELD_SEPARATOR, startPos );		
		
		if ( val.length!=endPos-startPos ) {
			return false;
		}		
		for (int i=0; i<val.length; i++) {
			if ( val[i]!=bufferArray[i+startPos]) {
				return false;
			}
		}		
		return true;
	}
	
	public FixMessage addHeader(FixTag fixTag, char[] val) {
		byte[] tag = fixTag.getTagBytes();		
		headerStart -= (tag.length+1+1);
				
		System.arraycopy(tag, 0, bufferArray, headerStart, tag.length);		
		headerLen += tag.length;
		
		for (int i=0; i<val.length; i++ ) {
			bufferArray[i+headerStart+tag.length] = (byte) (val[i] & 0xFF);
			bufferArray[i+headerStart+tag.length+1] = IFixConst.FIELD_SEPARATOR;
			headerLen+=2;
		}
		return this;
	}
	
	public FixMessage addHeader(FixTag fixTag, byte[] val) {
		byte[] tag = fixTag.getTagBytes();		
		headerStart -= (tag.length+1+1);
				
		System.arraycopy(tag, 0, bufferArray, headerStart, tag.length);		
		headerLen += tag.length;
		
		for (int i=0; i<val.length; i++ ) {
			bufferArray[i+headerStart+tag.length] = val[i];
			bufferArray[i+headerStart+tag.length+1] = IFixConst.FIELD_SEPARATOR;
			headerLen+=2;
		}
		return this;
	}
	
	public FixMessage addHeader(FixTag fixTag, int v) {
		return addHeader( fixTag, String.valueOf(v));
	}	
	
	public void addHeader(FixField fixField) {
		byte[] source = fixField.getBuffer().array();
		int sourceLen = fixField.getEndBuff()-fixField.getStartBuff();
		headerStart -= (sourceLen+1);
				
		System.arraycopy(source, fixField.getStartBuff(), bufferArray, headerStart, sourceLen );		
		headerLen += sourceLen;
		
		bufferArray[headerStart+sourceLen] = IFixConst.FIELD_SEPARATOR;
		headerLen++;
	}
	
	public FixMessage addHeader(FixTag fixTag, String v) {
		byte[] tag = fixTag.getTagBytes();
		byte[] val = v.getBytes();
		headerStart -= (tag.length+val.length+1);
				
		System.arraycopy(tag, 0, bufferArray, headerStart, tag.length);		
		headerLen += tag.length;
				
		System.arraycopy(val, 0, bufferArray, headerStart+tag.length, val.length);		
		headerLen += val.length;		
		
		bufferArray[headerStart+val.length+tag.length] = IFixConst.FIELD_SEPARATOR;	
		headerLen++;
	
		return this;
	}

	public FixGroup getFixGroup(FixTag fixTag) {
		FixGroup ret = new FixGroup( fixTag );
		ret.setCounter( getIntegerValue(fixTag) );		
		ret.setStartOffset( ArrayUtils.findArray( bufferArray, fixTag.getTagBytes() ) );
		return ret;
	}
	
	public int getIntegerValue( FixTag tag ) {
		int startPos = ArrayUtils.findArray( bufferArray, tag.getTagBytes() );
		if ( startPos<0 ) {
			return 0;
		}
		startPos += tag.getTagBytes().length;
		
		int endPos = ArrayUtils.findByte( bufferArray, IFixConst.FIELD_SEPARATOR, startPos );
		return ArrayUtils.toInteger( bufferArray, startPos, endPos );
	}
	
	public String getStringValue( FixTag tag ) {
		int startPos = ArrayUtils.findArray( bufferArray, tag.getTagBytes() );
		if ( startPos<0 ) {
			return null;
		}
		startPos += tag.getTagBytes().length;
		
		int endPos = ArrayUtils.findByte( bufferArray, IFixConst.FIELD_SEPARATOR, startPos );
		return new String( bufferArray, startPos, endPos-startPos );
	}
	
	public String getStringValue( FixGroup group, FixTag tag ) {
		int startPos = ArrayUtils.findArray( bufferArray, tag.getTagBytes(), group.getCurrentOffset() );
		if ( startPos<0 ) {
			return null;
		}
		startPos += tag.getTagBytes().length;
		int endPos = ArrayUtils.findByte( bufferArray, IFixConst.FIELD_SEPARATOR, startPos );
		
		group.moveForward(endPos);
		return new String( bufferArray, startPos, endPos-startPos );
	}
	
	public FixField getField( FixTag tag ) {
		return getField( tag, new FixField() );	
	}
	
	public FixField getField( FixTag tag, FixField field ) {
		byte[] tagBytes = tag.getTagBytes();
		int startTag = ArrayUtils.findArray( bufferArray, tagBytes );
		if ( startTag<0 ) {
			return null;
		}
		int endTag = startTag + tagBytes.length;
		int endVal = ArrayUtils.findByte( bufferArray, IFixConst.FIELD_SEPARATOR, endTag );		
		return FixField.fromBuffer( tag, field, buffer, startTag, endTag, startTag, endVal, endTag, endVal );
	}

	public FixField getHederField( FixTag tag ) {
		return getField( tag, new FixField() );
	}
	
	public FixField getHederField( FixTag tag, FixField field ) {
		return getField( tag, field );
	}
		
	public FixMessage addField(FixField fixField) {
		byte[] source = fixField.getBuffer().array();
		int sourceLen = fixField.getEndBuff()-fixField.getStartBuff();
		System.arraycopy(source, fixField.getStartBuff(), bufferArray, bodyStart+bodyLen, sourceLen );
		bufferArray[bodyStart+bodyLen+sourceLen] = IFixConst.FIELD_SEPARATOR;
		bodyLen += sourceLen + 1;
		return this;
	}
	
	public FixMessage addField(FixTag fixTag, byte [] v) {
		return addField( fixTag, String.valueOf(v));
	}
	
	public FixMessage addField(FixTag fixTag, int v) {
		return addField( fixTag, NumbersCache.toString(v));
	}
	
	public FixMessage addField(FixTag fixTag, char v) {
		byte[] source = fixTag.getTagBytes();
		System.arraycopy(source, 0, bufferArray, bodyStart+bodyLen, source.length);		
		bodyLen += source.length;
		
		bufferArray[bodyStart+bodyLen] = (byte)(v & 0xFF);		
		bufferArray[bodyStart+bodyLen+1] = IFixConst.FIELD_SEPARATOR;		
		bodyLen+=2;		
		return this;
	}
		
	public FixMessage addField(FixTag fixTag, String v) {
		byte[] source = fixTag.getTagBytes();
		System.arraycopy(source, 0, bufferArray, bodyStart+bodyLen, source.length);		
		bodyLen += source.length;
		
		source = v.getBytes();
		System.arraycopy(source, 0, bufferArray, bodyStart+bodyLen, source.length);		
		bodyLen += source.length;
		
		bufferArray[bodyStart+bodyLen] = IFixConst.FIELD_SEPARATOR;		
		bodyLen++;		
		return this;
	}
	
	public FixMessage build( String beginString, String senderCompId, String targetCompId, String sendingTime, int outgoingSeq) {
		// Reversed order for header fields
		addHeader( IFixConst.StandardHeader.PossDupFlag, "N" );	
		addHeader( IFixConst.StandardHeader.MsgSeqNum, outgoingSeq );
		addHeader( IFixConst.StandardHeader.SendingTime, sendingTime );
		addHeader( IFixConst.StandardHeader.TargetCompID, targetCompId );
		addHeader( IFixConst.StandardHeader.SenderCompID, senderCompId );
		addHeader( IFixConst.StandardHeader.MsgType, msgType.getBytes() );
		addHeader( IFixConst.StandardHeader.BodyLen, bodyLen+headerLen );
		addHeader( IFixConst.StandardHeader.BeginString, beginString );		
		addField( IFixConst.StandardTrailer.CheckSum, calcChecksum() );		
		return this;
	}
	
	public String calcChecksum() {
		return NumbersCache.toPaddedString(MathUtils.calcChecksum(bufferArray, headerStart, headerStart+headerLen+bodyLen));
	}
	
	@Override
	public String toString() {
		return new String( bufferArray, 
			(headerLen+bodyLen)!=0?headerStart:0, 
			(headerLen+bodyLen)!=0?headerLen+bodyLen:buffer.limit() 
		);				
	}

	public int getBodyLen() {
		return bodyLen;
	}

	public int getHeaderLen() {
		return headerLen;
	}

	public int getHeaderStart() {
		return headerStart;
	}

	// Poolable
	@Override
	public String getPoolId() {
		return poolId;
	}

	// Poolable	
	@Override
	public Status getStatus() {
		return poolStatus;
	}
	
	@Override
	public FixMessage setStatus( Status s) {
		poolStatus = s;
		return this;
	}
	
	public FixMessage clone() {		
		int buffStart = headerStart; 
		int buffEnd = buffStart + headerLen + bodyLen;
		
		FixMessage newMsg = new FixMessage(null);
		newMsg.setMsgType( this.getMsgType().toString() );
		newMsg.setStatus( this.getStatus() );
				
		byte[] buff = new byte[ buffEnd-buffStart ];
		newMsg.buffer = ByteBuffer.wrap(buff);
		newMsg.bufferArray = newMsg.buffer.array();
		System.arraycopy(this.bufferArray, buffStart, newMsg.bufferArray, 0, buff.length);	
		
		newMsg.bodyLen = this.bodyLen;
		newMsg.headerLen = this.headerLen;
		newMsg.headerStart = 0;
		newMsg.bodyStart = this.headerLen + 2;
				
		return newMsg;		
	}

}
