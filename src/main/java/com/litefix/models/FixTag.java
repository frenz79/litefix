package com.litefix.models;

import com.litefix.caches.NumbersCache;

public class FixTag {
	
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
