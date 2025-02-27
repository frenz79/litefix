package com.litefix.connectors.coinbase.fix;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.connectors.AbstractConnector;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.AbstractFixSession;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

public class CoinbaseFixClient extends AbstractConnector {

	private static final String HOST_MARKET_DATA = "";
	private static final String HOST_MARKET_DATA_TEST = "fix-md.sandbox.exchange.coinbase.com:6121";
	
	private static final String PRIVATE_KEY_TEST = "";
	
	private static final String PRIVATE_KEY_FILE = "";
	
	private static final String API_KEY = "";
	private static final String API_KEY_TEST = "";
	
	private static final String API_KEY_TEST_PWD = "";
	
	private static final String ENCRYPTION_ALGO = "Ed25519";
	
	private PrivateKey privateKey;
	
	private ClientFixSession session;

	public static void main( String[] args ) throws Exception {
		System.setProperty("javax.net.debug", "ssl:handshake");
		new CoinbaseFixClient().start();
	}
	
	public void start() throws Exception {
		initLogging();
		
		this.privateKey = getPrivateKey( "", ENCRYPTION_ALGO );
	    
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig()
				.setSenderCompId( API_KEY_TEST )	// Client API key
				.setTargetCompId( "Coinbase" )
				.setHeartBtInt( 10 )
				.setResetSeqNumFlag('Y')
				.addServer(HOST_MARKET_DATA_TEST, 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( new FileInputStream("D:\\SourceCode\\Incubator\\litefix\\litefix-examples\\src\\main\\resources\\ssl\\binance\\binance.jks" ))
					.setTrustStoreCert( new FileInputStream("D:\\SourceCode\\Incubator\\litefix\\litefix-examples\\src\\main\\resources\\ssl\\binance\\binance.jks" ))
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( CoinbaseFixDictionary.init() );
			
		IClientTransport	transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		IPersistence<FixMessageEncoder>	persistence = new InMemoryPersistence<FixMessageEncoder>();
			
		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( false, true );
	}	

	public String calculateSignature(String plainText, PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("sha256");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signature = privateSignature.sign();

	    return Base64.getEncoder().encodeToString(signature);
	}

	@Override
	public FixMessageEncoder beforeMessageSnd(FixMessageEncoder encoder, AbstractFixSession session) {
		if ( !encoder.getMsgType().equals("A") ) {
			return encoder;
		}
		try {
			char fieldSep = session.getSessionConfig().getDictionary().getFieldSep();
			String sendingTime = TimeUtils.getSendingTime();
			String Username  = API_KEY_TEST;
			String Password  = API_KEY_TEST_PWD;
						
			String RawData = calculateSignature(
				sendingTime	+ fieldSep +
				encoder.getMsgType() + fieldSep +
				encoder.getSeqNum() + fieldSep + 
				session.getSessionConfig().getSenderCompId() + fieldSep +
				session.getSessionConfig().getTargetCompId() + fieldSep +
				Password ,
				this.privateKey
			);
			return encoder
					.set(553, Username)
					.set(554, Password)
					.set(96, RawData)
					.set(98, 0)
					.set(52, sendingTime)
					.set(1137, "9"); // DefaultApplVerID
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
