package com.litefix.models;

public class FixGroup {

	private final FixTag fixTag;
	private int counter;
	private int currentOffset;
	private int startOffset;
	
	public FixGroup(FixTag fixTag) {
		super();
		this.fixTag = fixTag;
	}

	public void setCounter(int v) {
		counter = v;
	}

	public int getCounter() {
		return counter;
	}

	public FixTag getFixTag() {
		return fixTag;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
		this.currentOffset = startOffset;
	}
	
	public void rewind() {
		this.currentOffset = startOffset;
	}

	public int getCurrentOffset() {
		return currentOffset;
	}

	public int getStartOffset() {
		return startOffset;
	}

	void moveForward(int currentOffset) {
		this.currentOffset = currentOffset;
	}

	@Override
	public String toString() {
		return "FixGroup [fixTag=" + fixTag + ", counter=" + counter + ", currentOffset=" + currentOffset
				+ ", startOffset=" + startOffset + "]";
	}
}
