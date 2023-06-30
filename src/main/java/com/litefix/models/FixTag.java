package com.litefix.models;

import com.litefix.caches.NumbersCache;

public class FixTag {

	public static final FixTag HEADER_TAG_SEQ_NUM = new FixTag(34);
	public static final FixTag HEADER_TAG_SENDING_TIME = new FixTag(52);
	public static final FixTag HEADER_TAG_BEGIN_STRING = new FixTag(8);
	public static final FixTag HEADER_TAG_BODY_LEN = new FixTag(9);
	public static final FixTag HEADER_TAG_MSG_TYPE = new FixTag(35);
	public static final FixTag HEADER_TAG_SENDER_COMP_ID = new FixTag(49);
	public static final FixTag HEADER_TAG_TARGET_COMP_ID = new FixTag(56);
		
	public static final FixTag BODY_TAG_CHECKSUM = new FixTag(10);
	
	public static final FixTag TAG_SYMBOL = new FixTag(55);
	public static final FixTag TAG_QUOTE_ID = new FixTag(117);
	
	public static final byte FIELD_SEPARATOR = '\001';
	
	private final int tagNum;
	private final byte[] tagBytes;
	
	public FixTag(int tagNum) {
		super();
		this.tagNum = tagNum;
		this.tagBytes = (NumbersCache.toString(tagNum) + "=" ).getBytes();
	}

	public int getTagNum() {
		return tagNum;
	}

	public byte[] getTagBytes() {
		return tagBytes;
	}
	
}
