package com.litefix.commons.collections;

import java.util.Arrays;

public class IndexedFifoQueue<T> {
	
	private final int maxSize;	
	private int maxIndex = 0;
	private int minIndex = 0;
	
	private Object[] storage;
	private int[] indexes;
	
	public IndexedFifoQueue( int maxSize ) {
		this.maxSize = maxSize;	
		this.storage = new Object[ maxSize ];
		this.indexes = new int[ maxSize ];
	}
		
	public void put( int index, T value ) {
		this.indexes[index%maxSize] = index;
		this.storage[index%maxSize] = value;
		
		if ( index>maxIndex ) {
			maxIndex = index;
			minIndex = this.indexes[(index+1)%maxSize];
		} 
	}
	
	@SuppressWarnings("unchecked")
	public T get( int index ) {
		if ( index>maxIndex ) return null;
		if ( index<minIndex ) return null;
		return (T)this.storage[ index%maxSize ];
	}

	public void clear( ) {
		maxIndex = 0;
		minIndex = 0;
		Arrays.fill(indexes, 0);
	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getMaxIndex() {
		return maxIndex;
	}

	public int getMinIndex() {
		return minIndex;
	}
}
