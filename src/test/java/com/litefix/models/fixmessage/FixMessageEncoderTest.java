package com.litefix.models.fixmessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.litefix.models.dictionary.DefaultFix44Dictionary;

class FixMessageEncoderTest {

	@Test
	void testFixMessageEncoder() {
		String fixMsg = "8=FIX.4.49=7635=A49=BuySide56=SellSide34=152=20190605-11:27:06.89798=0108=30141=Y10=010";
		
		FixMessageEncoder dec = FixMessageEncoder.newEncoder( DefaultFix44Dictionary.init(), "A" )
			.set(49, "BuySide")
			.set(56, "SellSide")
			.set(34, 1)
			.set(52, "20190605-11:27:06.897")
			.set(98, 0)
			.set(108, 30)
			.set(141, 'Y')
		;
		
		assertEquals(fixMsg, dec.buildString());
	}

}
