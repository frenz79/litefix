package com.litefix.connectors.binance;

import java.io.FileInputStream;

import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;

public class BinanceClient {
	/**
	 * TEST:
	 *   - PRIVATE_KEY_FILE_TEST
	 *   - HOST_MARKET_DATA_TEST
	 *   - API_KEY_TEST2
	 *   - SSL_CERT_TEST
	 *   
	 * PROD:
	 *   - PRIVATE_KEY_FILE
	 *   - HOST_MARKET_DATA
	 *   - API_KEY
	 *   - SSL_CERT
	 * 
	 */
	
	private static final String HOST_MARKET_DATA = "fix-md.binance.com";
//	private static final String HOST_MARKET_DATA_TEST = "fix-md.testnet.binance.vision";
	
	private static final String PRIVATE_KEY_FILE = "\\resources\\ssl\\binance\\prv-key.pem";
//	private static final String PRIVATE_KEY_FILE_TEST = "\\resources\\ssl\\binance\\prv-key-test.pem;
	
	private static final String API_KEY = "Ta5lqCvSvTOBYA858YQweZzyOgqbuAci6CkxhEsMCVMEnjtrMd6KXd9fbEccF233";
	
	private static final String API_KEY_TEST = "v43WpvjS7B9ovwGbFr4AL58RlvUiSrSq4UYpjKug96a7SBYbBDleslYBuwpmjNFa";
	
	private static final String SSL_CERT_TEST = "\\resources\\ssl\\binance\\binance.vision.jks";
	private static final String SSL_CERT = "\\resources\\ssl\\binance\\binance.jks";
	
	private final BinanceFixClient fixClient;
		
	public BinanceClient( String binanceEnv, String apiKey, String privateKey ){
		this.fixClient = new BinanceFixClient();
		
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOTTEST" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 30 )
				.setResetSeqNumFlag('Y')
				.addServer(HOST_MARKET_DATA, 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( new FileInputStream(SSL_CERT))
					.setTrustStoreCert( new FileInputStream(SSL_CERT))
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( BinanceFixDictionary.init() );
	}
	
	public static void main( String[] args ) throws Exception {
		String usage = "--env [test|prod] --apiKey [your api key] --privateKey [your private key]";
		
		if (args.length == 0) {
            System.out.println("No arguments provided.");
            return;
        }

        String env = null;
        String apiKey = null;
        String privateKey = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--env":
                    if (i + 1 < args.length) {
                    	env = args[++i];
                    } else {
                        System.out.println("Error: --env requires a value");
                    }
                    break;
                case "--apiKey":
                    if (i + 1 < args.length) {
                       apiKey = args[++i];
                    } else {
                        System.out.println("Error: --apiKey requires a value");
                    }
                    break;
                case "--privateKey":
                    if (i + 1 < args.length) {
                    	privateKey = args[++i];
                    } else {
                        System.out.println("Error: --privateKey requires a value");
                    }
                    break;
                default:
                    System.out.println("Unknown argument: " + args[i]);
                    System.out.println(usage);
                    System.exit(-1);
            }
        }

        if ( env==null || apiKey==null || privateKey==null) {
        	 System.out.println("Missing one or more params");
             System.out.println(usage);
             System.exit(-1);
        }
        
        System.out.println("env: " + env);
        System.out.println("apiKey: " + apiKey);
        System.out.println("privateKey: " + privateKey);
        
		new BinanceFixClient().start();
	}
}
