package com.litefix.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class FixFieldTest {

	@Test
	public void testCompreTo() {
		FixField f1 = new FixField(123, "ExampleString");
		
		assertEquals( f1.compareTo(new FixField(123, "ExampleString")), 0 );
		assertEquals( f1.compareTo(new FixField(123, "ExampleString1")), -1 );
		assertEquals( f1.compareTo(new FixField(123, "ExampleStrin")), 1 );
	}
	
	@Test
	public void testIsString() {
		FixField f1 = new FixField(123, "ExampleString");
		
		assertTrue( f1.is("ExampleString") );
		assertFalse( f1.is("ExampleString1") );
		assertFalse( f1.is("ExampleStrin") );
		assertFalse( f1.is("") );
	}
	
	@Test
	public void testIsInteger() {
		FixField f1 = new FixField(123, "1");
		
		assertTrue( f1.is("1") );
		assertFalse( f1.is("2") );
		assertFalse( f1.is('1') );
		assertFalse( f1.is("") );
	}
	
	@Test
	public void testContainsString() {
		FixField f1 = new FixField(123, "ExampleString");
		
		assertTrue( f1.contains("ExampleString") );
		assertFalse( f1.contains("ExampleString1") );
		assertTrue( f1.contains("ExampleStrin") );
		assertTrue( f1.contains("") );
	}
}
