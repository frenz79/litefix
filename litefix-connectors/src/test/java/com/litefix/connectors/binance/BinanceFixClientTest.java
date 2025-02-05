package com.litefix.connectors.binance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.litefix.connectors.Book;
import com.litefix.connectors.Trade;
import com.litefix.models.dictionary.DefaultFix44Dictionary;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;

class BinanceFixClientTest {

	private static final String PRIVATE_KEY_TEST = 
			"-----BEGIN PRIVATE KEY-----\r\n"
			+ "MC4CAQAwBQYDK2VwBCIEIIJEYWtGBrhACmb9Dvy+qa8WEf0lQOl1s4CLIAB9m89u\r\n"
			+ "-----END PRIVATE KEY-----";
	
	@Test
	void testCalculateSignature() throws Exception {
		BinanceFixClient c = new BinanceFixClient() {

			@Override
			public void onMarketData(Book m) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onMarketData(Trade m) {
				// TODO Auto-generated method stub
			}
			
		};
		
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

	@Test
	void validateSessionConfig() throws Exception {
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOTTEST" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 30 )
				.setResetSeqNumFlag('Y')
				.addServer("dummy", 9000)
				.enableSSL( new SSLSettings()
		//			.setKeyStoreCert( new FileInputStream(SSL_CERT_TEST))
		//			.setTrustStoreCert( new FileInputStream(SSL_CERT_TEST))
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( BinanceFixDictionary.init() );
		
		ClientFixSession session = (ClientFixSession) new ClientFixSession( null, null, sessionCfg );
	}	
	
	// 8=FIX.4.49=000093035=W49=SPOT56=SPOTTEST34=252=20250205-18:15:38.258754262=3c95b73d-fa9a-42b6-ac72-7e9c1bb0217b
	// 55=BTCUSDT25044=300920268=20269=0270=96889.99000000271=0.00562000269=0270=96889.98000000271=0.00403000269=0
	// 270=96889.43000000271=0.00357000269=0270=96869.96000000271=0.00152000269=0270=96869.60000000271=0.00450000
	// 269=0270=96863.86000000271=0.00262000269=0270=96863.66000000271=0.00382000269=0270=96863.21000000271=0.00326000
	// 269=0270=96863.20000000271=0.00372000269=0270=96767.41000000271=0.00010000269=1270=96909.92000000271=0.00494000
	// 269=1270=96913.67000000271=0.00284000269=1270=96913.95000000271=0.00439000269=1270=96913.96000000271=0.00491000
	// 269=1270=96972.00000000271=0.00175000269=1270=96972.60000000271=0.00305000269=1270=96974.01000000271=0.00294000
	// 269=1270=96998.98000000271=0.00464000269=1270=96998.99000000271=0.00372000269=1270=96999.00000000271=0.00310000
	// 10=105
	@Test
	void decodeMarketDataSnapshot() throws Exception {
		
	}

}
