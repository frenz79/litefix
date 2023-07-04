package com.litefix.commons.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IndexedFifoQueueTest {

	@Test
	public void test1() {
		IndexedFifoQueue<String> map = new IndexedFifoQueue<String>(5);
		
		map.put(0, "0");
		map.put(1, "1");
		map.put(2, "2");
		map.put(3, "3");
		map.put(4, "4");
		
		assertEquals(map.getMaxIndex(), 4);
		assertEquals(map.getMinIndex(), 0);
				
		map.put(5, "5");
		
		assertEquals(map.getMaxIndex(), 5);
		assertEquals(map.getMinIndex(), 1);
		
		map.put(6, "6");
		
		assertEquals(map.getMaxIndex(), 6);
		assertEquals(map.getMinIndex(), 2);
		
		map.clear();
		
		assertEquals(map.getMaxIndex(), 0);
		assertEquals(map.getMinIndex(), 0);
		
		map.put(0, "0");
		map.put(1, "1");
		
		assertEquals(map.getMaxIndex(), 1);
		assertEquals(map.getMinIndex(), 0);
		
		map.put(0, "0");
		map.put(1, "1");
		
		assertEquals(map.getMaxIndex(), 1);
		assertEquals(map.getMinIndex(), 0);
		
	}
}
