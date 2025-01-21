package com.litefix.connectors.binance;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

import com.litefix.commons.utils.TimeUtils;
import com.litefix.connectors.AbstractConnector;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

public class BinanceFixClient extends AbstractConnector {

	private static final String HOST_MARKET_DATA = "fix-md.binance.com";
	private static final String HOST_MARKET_DATA_TEST = "fix-md.testnet.binance.vision";
	
	private static final String PRIVATE_KEY_FILE = "D:\\SourceCode\\Incubator\\litefix\\litefix-connectors\\src\\main\\resources\\ssl\\binance\\Private_key.txt";
	
	private static final String PRIVATE_KEY_FILE_TEST = "D:\\SourceCode\\Incubator\\litefix\\litefix-connectors\\src\\main\\resources\\ssl\\binance\\test-prv-key.pem";
	
	private static final String API_KEY = "Ta5lqCvSvTOBYA858YQweZzyOgqbuAci6CkxhEsMCVMEnjtrMd6KXd9fbEccF233";
	private static final String API_KEY_TEST = "ZlCcXB71oXDqDqqKjzVATt6ePfAEOpQHuOx0MbioHwaZLMtVouyrPySdhxjZ3Ao0";
	
	private static final String API_KEY_TEST2 = "v43WpvjS7B9ovwGbFr4AL58RlvUiSrSq4UYpjKug96a7SBYbBDleslYBuwpmjNFa";
	
	private static final String SSL_CERT_TEST = "D:\\SourceCode\\Incubator\\litefix\\litefix-connectors\\src\\main\\resources\\ssl\\binance\\binance.vision.jks";
	private static final String SSL_CERT = "D:\\SourceCode\\Incubator\\litefix\\litefix-connectors\\src\\main\\resources\\ssl\\binance\\binance.jks";
	
	private ClientFixSession session;
	private PrivateKey privateKey;
	
	public static void main( String[] args ) throws Exception {
		System.setProperty("javax.net.debug", "all");
		new BinanceFixClient().start();
	}
	
	public void start() throws Exception {
		initLogging();
	    
		this.privateKey = getPrivateKey(new File(PRIVATE_KEY_FILE_TEST), "Ed25519");
				
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOT-SESSION-TEST" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 30 )
				.setResetSeqNumFlag('Y')
				.addServer(HOST_MARKET_DATA_TEST, 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( new FileInputStream(SSL_CERT_TEST))
					.setTrustStoreCert( new FileInputStream(SSL_CERT_TEST))
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( BinanceFixDictionary.init() );
			
		IClientTransport	transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		IPersistence<FixMessageEncoder>	persistence = new InMemoryPersistence<FixMessageEncoder>();
			
		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( true, true );
	}	

	public String calculateSignature(String plainText, PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("Ed25519");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signature = privateSignature.sign();

	    return Base64.getEncoder().encodeToString(signature);
	}
	
	private final int MessageHandling = 2;
	private final int EncryptMethod = 0;
	
	/**
	 The signature payload is a text string constructed by concatenating the values of the following fields in this exact order, separated by the SOH character:
		MsgType (35)
		SenderCompId (49)
		TargetCompId (56)
		MsgSeqNum (34)
		SendingTime (52) 
	 **/
	@Override
	public FixMessageEncoder beforeMessageSnd(FixMessageEncoder encoder) {
		try {
			char fieldSep = session.getSessionConfig().getDictionary().getFieldSep();
			String sendingTime = TimeUtils.getSendingTime();
			String Username  = API_KEY_TEST2;
			String RawData = calculateSignature(
				encoder.getMsgType() + fieldSep +
				session.getSessionConfig().getSenderCompId() + fieldSep +
				session.getSessionConfig().getTargetCompId() + fieldSep +
				encoder.getSeqNum() + fieldSep +
				sendingTime ,
				this.privateKey
			);
			return encoder
				.set(553, Username)
				.set(96, RawData)
				.set(95, RawData.length())
				.set(25035, MessageHandling)
				.set(98, EncryptMethod)
				.set(52, sendingTime); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
