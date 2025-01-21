package com.litefix.connectors.binance;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.litefix.models.dictionary.DefaultFix44Dictionary;

class BinanceFixClientTest {

	private static final String PRIVATE_KEY_TEST = 
			"-----BEGIN PRIVATE KEY-----\r\n"
			+ "MC4CAQAwBQYDK2VwBCIEIIJEYWtGBrhACmb9Dvy+qa8WEf0lQOl1s4CLIAB9m89u\r\n"
			+ "-----END PRIVATE KEY-----";
	
	@Test
	void testCalculateSignature() throws Exception {
		BinanceFixClient c = new BinanceFixClient();
		
		String signature = c.calculateSignature(
				"A" + DefaultFix44Dictionary.init().getFieldSep() +
				"EXAMPLE" + DefaultFix44Dictionary.init().getFieldSep() +
				"SPOT" + DefaultFix44Dictionary.init().getFieldSep() +
				"1" + DefaultFix44Dictionary.init().getFieldSep() +
				"20240627-11:17:25.223" ,
				c.getPrivateKey(PRIVATE_KEY_TEST, "Ed25519")
			);
		
		assertEquals("4MHXelVVcpkdwuLbl6n73HQUXUf1dse2PCgT1DYqW9w8AVZ1RACFGM+5UdlGPrQHrgtS3CvsRURC1oj73j8gCA==", signature);
	}

	
}
