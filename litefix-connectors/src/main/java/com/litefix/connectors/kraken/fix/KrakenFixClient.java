package com.litefix.connectors.kraken.fix;

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
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.models.session.SSLSettings;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

public class KrakenFixClient extends AbstractConnector {

	private static final String HOST_MARKET_DATA = " ";
	private static final String HOST_MARKET_DATA_TEST = " ";
		
	private static final String API_KEY = " ";
	private static final String PRIVATE_KEY = "";
	
	private ClientFixSession session;

	public static void main( String[] args ) throws Exception {
		System.setProperty("javax.net.debug", "ssl:handshake");
		new KrakenFixClient().start();
	}
	
	public void start() throws Exception {
		initLogging();
	    
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig()
				.setSenderCompId( "SPOT-SESSION-TEST" )
				.setTargetCompId( "SPOT" )
				.setHeartBtInt( 60 )
				.setResetSeqNumFlag('Y')
				.addServer(HOST_MARKET_DATA_TEST, 9000)
				.enableSSL( new SSLSettings()
					.setKeyStoreCert( new FileInputStream("D:\\SourceCode\\Incubator\\litefix\\litefix-examples\\src\\main\\resources\\ssl\\binance\\binance.jks" ))
					.setTrustStoreCert( new FileInputStream("D:\\SourceCode\\Incubator\\litefix\\litefix-examples\\src\\main\\resources\\ssl\\binance\\binance.jks" ))
					.setKeyStorePwd("password")
					.setTrustStorePwd("password")
					.setUseInsecureTrustManager(true)
				)
				.setDictionary( KrakenFixDictionary.init() );
			
		IClientTransport	transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		IPersistence<FixMessageEncoder>	persistence = new InMemoryPersistence<FixMessageEncoder>();
			
		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( false, true );
	}	

	public String calculateSignature(String plainText, PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("Ed25519");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signature = privateSignature.sign();

	    return Base64.getEncoder().encodeToString(signature);
	}
	
	@Override
	public void onConnect(boolean upOrDown) {
		if (upOrDown) {
			try {
				char fieldSep = session.getSessionConfig().getDictionary().getFieldSep();
				String sendingTime = TimeUtils.getSendingTime();
				int ClientID;
				String Username = API_KEY;
				String Password = "";
				String RawData = calculateSignature(
					"A" + fieldSep +
					session.getSessionConfig().getSenderCompId() + fieldSep +
					"SPOT" + fieldSep +
					"1" + fieldSep +
					sendingTime ,
					getPrivateKey(new File(PRIVATE_KEY), "Ed25519"));
				
				int MessageHandling = 1;
				int EncryptMethod = 0;
				
				FixMessageEncoder  logon = session.newEncoder("A")
					.set(109, 109)
					.set(553, Username)
					.set(554, Password)
					.set(96, RawData)
					.set(25035, MessageHandling)
					.set(98, EncryptMethod)
					.set(52, sendingTime)
				;
			 
				session.doLogon( logon );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Connection DOWN!");
		}
	}

}
