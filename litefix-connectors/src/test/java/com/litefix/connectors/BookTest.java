package com.litefix.connectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BookTest {

	@Test
	void addLevelTest() {
		Book<Long,Long> b = new Book<>("", "", "", 5);
		
		b.addLevel( 10l, 1l, true );
		b.addLevel( 11l, 1l, true );
		b.addLevel( 12l, 1l, true );
		b.addLevel( 13l, 1l, true );
		b.addLevel( 14l, 1l, true );
				
		b.addLevel( 15l, 1l, false );
		b.addLevel( 16l, 1l, false );
		b.addLevel( 17l, 1l, false );
		b.addLevel( 18l, 1l, false );
		b.addLevel( 19l, 1l, false );
		
		assertEquals(5, b.getAskLevels().size());
		assertEquals(5, b.getBidLevels().size());
		
		// Check for best
		assertTrue( b.getBestLevel(true).getPrice().compareTo(14l)==0 );
		assertTrue( b.getBestLevel(false).getPrice().compareTo(15l)==0 );
		// ..and the tail
		assertTrue( b.getBidLevel(4).getPrice().compareTo(10l)==0 );
		assertTrue( b.getAskLevel(4).getPrice().compareTo(19l)==0 );
	}
		
	@Test
	void addLevelTest2() {
		Book<Long,Long> b = new Book<>("", "", "", 5);
		b.addLevel( 12l, 1l, true );
		b.addLevel( 11l, 1l, true );
		b.addLevel( 10l, 1l, true );
		b.addLevel( 14l, 1l, true );
		b.addLevel( 13l, 1l, true );
		
		b.addLevel( 18l, 1l, false );
		b.addLevel( 15l, 1l, false );
		b.addLevel( 17l, 1l, false );
		b.addLevel( 19l, 1l, false );
		b.addLevel( 16l, 1l, false );
		
		assertEquals(5, b.getAskLevels().size());
		assertEquals(5, b.getBidLevels().size());
		
		// Check for best
		assertTrue( b.getBestLevel(true).getPrice().compareTo(14l)==0 );
		assertTrue( b.getBestLevel(false).getPrice().compareTo(15l)==0 );
		// ..and the tail
		assertTrue( b.getBidLevel(4).getPrice().compareTo(10l)==0 );
		assertTrue( b.getAskLevel(4).getPrice().compareTo(19l)==0 );
	}

}
