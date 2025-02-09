package com.litefix.connectors.binance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.md.Book;
import com.litefix.models.md.Order;
import com.litefix.models.md.Trade;
import com.litefix.models.md.enums.OrderType;
import com.litefix.models.md.enums.Side;
import com.litefix.models.md.enums.TimeInForce;
import com.litefix.models.session.AbstractFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;

public class BinanceClient {
		
	private static final String HOST_MARKET_DATA_PROD = "fix-md.binance.com";
	private static final String HOST_MARKET_DATA_TEST = "fix-md.testnet.binance.vision";
	
	private static final String HOST_MARKET_ORDERS_PROD = "fix-oe.binance.com";
	private static final String HOST_MARKET_ORDERS_TEST = "fix-oe.testnet.binance.vision";
	
	private String getBinanceHost( String env, boolean isInfo ) {
		if ("prod".equalsIgnoreCase(env)){
			return isInfo?HOST_MARKET_DATA_PROD:HOST_MARKET_ORDERS_PROD;
		}
		return isInfo?HOST_MARKET_DATA_TEST:HOST_MARKET_ORDERS_TEST;
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
		this.fixClient = new BinanceFixClient( new BinanceFixMapper() ) {
			@Override
			public void onLogon(FixMessageDecoder decoder, AbstractFixSession session, boolean result)
					throws SessionRejectMessageException, BusinessRejectMessageException {
				String symbol = "BTCUSDT";
				
				if ( (Boolean)session.getCustomAttribute("SESSION_INFO") ) {
					System.out.println("Subscribing Book for: " + symbol);
					subscribeBook(symbol, UUID.randomUUID().toString(), 1000);
			
					System.out.println("Subscribing Trades for: " + symbol);
					subscribeTrades(symbol, UUID.randomUUID().toString());
				} else {
					Order order = new Order()
						.setOrderId(UUID.randomUUID().toString())
						.setOrderType(OrderType.MARKET)
						.setQty(1L)
						.setSide(Side.BUY)
						.setSymbol(symbol)
						.setTimeInForce(TimeInForce.FILL_OR_KILL)
						;
					sendNewOrder( order );
				}
			}

			@Override
			public void onMarketData(Book m) {
				System.out.println(m);
			}
			
			@Override
			public void onMarketData(Trade t) {
				System.out.println(t);
			}
		};
		
		ClientFixSessionConfig sessionInfoCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOT-INF" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 30 )
				.setResetSeqNumFlag('Y')
				.addServer(getBinanceHost(binanceEnv, true), 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( getBinanceSSLCert(binanceEnv) )
					.setTrustStoreCert( getBinanceSSLCert(binanceEnv) )
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( BinanceFixDictionary.init() )
				.setCustomAttributes("SESSION_INFO", Boolean.TRUE);
		
		ClientFixSessionConfig sessionTrxCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOT-TRX" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 30 )
				.setResetSeqNumFlag('Y')
				.addServer(getBinanceHost(binanceEnv, false), 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( getBinanceSSLCert(binanceEnv) )
					.setTrustStoreCert( getBinanceSSLCert(binanceEnv) )
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( BinanceFixDictionary.init() )
				.setCustomAttributes("SESSION_INFO", Boolean.FALSE);
		
		this.fixClient.start(privateKey, apiKey, sessionInfoCfg, sessionTrxCfg);
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
