package com.litefix.models.fixmessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.litefix.models.dictionary.DefaultFix44Dictionary;

class FixMessageDecoderTest {

	@Test
	void test() {
		byte[] fixMsg = "8=FIX.4.49=7935=A49=senderCompId56=targetCompId52=2023010134=12343=N98=0108=10141=Y10=052".getBytes();
		
		FixMessageDecoder dec = FixMessageDecoder.newDecoder( DefaultFix44Dictionary.init(), fixMsg, 0l );
		assertEquals( "FIX.4.4", dec.asString(8) );
		assertEquals( "A", dec.asString(35) );	
		assertEquals( "senderCompId", dec.asString(49) );
		assertEquals( "targetCompId", dec.asString(56) );
		assertEquals( "20230101", dec.asString(52) );
		assertEquals( 'N', dec.asChar(43) );
		assertEquals( 'Y', dec.asChar(141) );
	}

}
