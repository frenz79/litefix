package com.litefix.connectors.binance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.UUID;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.connectors.Book;
import com.litefix.connectors.Trade;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;

public class BinanceClient {
		
	private static final String HOST_MARKET_DATA_PROD = "fix-md.binance.com";
	private static final String HOST_MARKET_DATA_TEST = "fix-md.testnet.binance.vision";
	
	private String getBinanceHost( String env ) {
		if ("prod".equalsIgnoreCase(env)){
			return HOST_MARKET_DATA_PROD;
		}
		return HOST_MARKET_DATA_TEST;
	}
	
	private static final String SSL_CERT_PROD = "src\\main\\resources\\ssl\\binance\\binance.jks";
	private static final String SSL_CERT_TEST = "src\\main\\resources\\ssl\\binance\\binance.vision.jks";
		
	private InputStream getBinanceSSLCert( String env ) throws FileNotFoundException {
		if ("prod".equalsIgnoreCase(env)){
			return new FileInputStream( SSL_CERT_PROD );
		}
		return new FileInputStream( SSL_CERT_TEST );
	}
		
	private final BinanceFixClient fixClient;
		
	public BinanceClient( String binanceEnv, String apiKey, String privateKey ) throws Exception{
		this.fixClient = new BinanceFixClient() {
			@Override
			public void onLogon(FixMessageDecoder decoder, boolean result)
					throws SessionRejectMessageException, BusinessRejectMessageException {
				String symbol = "BTCUSDT";
				
				System.out.println("Subscribing Book for: " + symbol);
				subscribeBook(symbol, UUID.randomUUID().toString(), 10);
				
			//	System.out.println("Subscribing Trades for: " + symbol);
			//	subscribeTrades(symbol, UUID.randomUUID().toString());
			}

			@Override
			public void onMarketData(Book<BigDecimal,BigDecimal> m) {
				System.out.println(m);
			}
			
			@Override
			public void onMarketData(Trade m) {
				System.out.println(m);
			}
		};
		
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOTTEST" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 30 )
				.setResetSeqNumFlag('Y')
				.addServer(getBinanceHost(binanceEnv), 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( getBinanceSSLCert(binanceEnv) )
					.setTrustStoreCert( getBinanceSSLCert(binanceEnv) )
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( BinanceFixDictionary.init() );
		
		this.fixClient.start(privateKey, apiKey, sessionCfg);
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
        
		new BinanceClient( env, apiKey, privateKey );
	}
}
