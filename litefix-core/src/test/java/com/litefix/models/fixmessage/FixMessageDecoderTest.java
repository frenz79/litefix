package com.litefix.models.fixmessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.litefix.models.dictionary.DefaultFix44Dictionary;
import com.litefix.models.fixmessage.FixMessageDecoder.GroupDecoder;

class FixMessageDecoderTest {

	@Test
	void simpleDecoderTest() {
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
	

	@Test
	void simpleGroupDecoderTest() {
		byte[] fixMsg = ("8=FIX.4.49=000021335=W49=SPOT56=SPOTTEST34=252=20250130-18:59:55.088145"
				+ "262=338a6a9f-cd55-42fb-a7c0-85aae0602a6455=BTCUSDT25044=10762840268=2"
				+ "269=0270=105427.98000000271=0.00276000269=1270=105427.99000000271=0.0036600010=182")
				.getBytes();
	
		FixMessageDecoder dec = FixMessageDecoder.newDecoder( DefaultFix44Dictionary.init(), fixMsg, 0l );
		
		GroupDecoder levelsDecoder = dec.asGroupDecoder(268); // NoMDEntries
		assertEquals( 2, levelsDecoder.getGroupSize() );

		{
			char side = levelsDecoder.at(0).asChar(269);
			BigDecimal price = levelsDecoder.at(0).asBigDecimal(270);	// MDEntryPx
			BigDecimal qty = levelsDecoder.at(0).asBigDecimal(271);		// MDEntrySize
			
			assertEquals( '0', side );
			assertEquals( new BigDecimal("105427.98000000"), price );
			assertEquals( new BigDecimal("0.00276000"), qty );
		}
		
		{
			char side = levelsDecoder.at(1).asChar(269);
			BigDecimal price = levelsDecoder.at(1).asBigDecimal(270);	// MDEntryPx
			BigDecimal qty = levelsDecoder.at(1).asBigDecimal(271);		// MDEntrySize
			
			assertEquals( '1', side );
			assertEquals( new BigDecimal("105427.99000000"), price );
			assertEquals( new BigDecimal("0.00366000"), qty );
		}
	}
}
