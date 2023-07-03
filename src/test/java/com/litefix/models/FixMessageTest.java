package com.litefix.models;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class FixMessageTest {

	
	@Test
	public void testToString() {
		FixMessage msg = new FixMessage( "1" )
				.setMsgType("A")
				.addField( new FixTag(98), "0" );
		
		assertEquals( msg.getBodyLen(), 5);
		
		msg.addField( new FixTag(99), "0" );
		
		assertEquals( msg.getBodyLen(), 10);
				
		assertEquals( msg.toString(), "98=099=0" );
	}
	
	@Test
	public void testCompreTo() {
		FixField holder = new FixField();
		FixMessage msg = new FixMessage( "1" )
		.setMsgType("A")
		.addField( new FixTag(98), "0" ) // EncryptMethod
		.addField( new FixTag(108), 10 )
		.addField( new FixTag(141), 'Y')
		.build("beginString", "senderCompId", "targetCompId", "20230101", 123);
		
		assertTrue( msg.getMsgType().is("A") );
		assertFalse( msg.getMsgType().is("B") );
		assertFalse( msg.getMsgType().is("a") );
		assertEquals(msg.getField(new FixTag(98), holder).valueAsString(), "0" );
		assertEquals(msg.getField(new FixTag(108), holder).valueAsString(), "10" );
		assertEquals(msg.getField(new FixTag(141), holder).valueAsString(), "Y" );
		
		assertEquals(msg.toString(), "8=beginString9=7435=A49=senderCompId56=targetCompId52=2023010134=12398=0108=10141=Y10=060");		
		
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_SENDER_COMP_ID, holder).valueAsString(), "senderCompId" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_TARGET_COMP_ID, holder).valueAsString(), "targetCompId" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_BEGIN_STRING, holder).valueAsString(), "beginString" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_SENDING_TIME, holder).valueAsString(), "20230101" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_SEQ_NUM, holder).valueAsString(), "123" );
				
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_SENDER_COMP_ID, holder).is("senderCompId") );
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_TARGET_COMP_ID, holder).is("targetCompId") );
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_BEGIN_STRING, holder).is("beginString") );		
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_SENDING_TIME, holder).is("20230101") );
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_SEQ_NUM, holder).is("123") );
	}
	
	@Test
	public void testPoll() {
		FixField holder = new FixField();
		FixMessage msg = new FixMessage( "1" );
		String fixMsg = "8=beginString9=7435=A49=senderCompId56=targetCompId52=2023010134=12398=0108=10141=Y10=060";
		
		System.arraycopy(fixMsg.getBytes(), 0,	msg.getBuffer().array(), 0, fixMsg.getBytes().length);
		msg.getBuffer().limit(fixMsg.getBytes().length); // hack
		
		assertTrue( msg.getMsgType().is("A") );
		assertFalse( msg.getMsgType().is("B") );
		assertFalse( msg.getMsgType().is("a") );
		assertEquals(msg.getField(new FixTag(98), holder).valueAsString(), "0" );
		assertEquals(msg.getField(new FixTag(108), holder).valueAsString(), "10" );
		assertEquals(msg.getField(new FixTag(141), holder).valueAsString(), "Y" );
		
		assertEquals(msg.toString(), "8=beginString9=7435=A49=senderCompId56=targetCompId52=2023010134=12398=0108=10141=Y10=060");		
		
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_SENDER_COMP_ID, holder).valueAsString(), "senderCompId" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_TARGET_COMP_ID, holder).valueAsString(), "targetCompId" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_BEGIN_STRING, holder).valueAsString(), "beginString" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_SENDING_TIME, holder).valueAsString(), "20230101" );
		assertEquals(msg.getHederField(FixTag.HEADER_TAG_SEQ_NUM, holder).valueAsString(), "123" );
				
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_SENDER_COMP_ID, holder).is("senderCompId") );
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_TARGET_COMP_ID, holder).is("targetCompId") );
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_BEGIN_STRING, holder).is("beginString") );		
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_SENDING_TIME, holder).is("20230101") );
		assertTrue( msg.getHederField(FixTag.HEADER_TAG_SEQ_NUM, holder).is("123") );
	}
}
