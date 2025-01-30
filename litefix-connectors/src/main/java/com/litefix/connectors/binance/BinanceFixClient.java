package com.litefix.connectors.binance;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

import com.litefix.commons.utils.TimeUtils;
import com.litefix.connectors.AbstractConnector;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.ClientFixSession;
import com.litefix.models.session.ClientFixSessionConfig;
import com.litefix.modules.persistence.IPersistence;
import com.litefix.modules.persistence.InMemoryPersistence;
import com.litefix.modules.transport.ClientSocketTransport;
import com.litefix.modules.transport.IClientTransport;

/**
 * See https://github.com/binance/binance-spot-api-docs/blob/master/testnet/fix-api.md 
 * 
 */
public class BinanceFixClient extends AbstractConnector {
	
	private ClientFixSession session;
	private PrivateKey privateKey;
	private String apiKey;
	
	public BinanceFixClient start( String privateKey, String apiKey, ClientFixSessionConfig sessionCfg ) throws Exception {
		initLogging();
	    this.apiKey = apiKey;
		this.privateKey = getPrivateKey(privateKey, "Ed25519");
			
		IClientTransport	transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		IPersistence<FixMessageEncoder>	persistence = new InMemoryPersistence<FixMessageEncoder>();
			
		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( true, true );
		return this;
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
		if ( !encoder.getMsgType().equals("A") ) {
			return encoder;
		}
		try {
			char fieldSep = session.getSessionConfig().getDictionary().getFieldSep();
			String sendingTime = TimeUtils.getSendingTime();
			String Username  = apiKey;
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
				.set(52, sendingTime); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// MarketDataSnapshot<W>
	// RCV: 8=FIX.4.49=000021335=W49=SPOT56=SPOTTEST34=252=20250130-18:59:55.088145262=338a6a9f-cd55-42fb-a7c0-85aae0602a6455=BTCUSDT25044=10762840268=2269=0270=105427.98000000271=0.00276000269=1270=105427.99000000271=0.0036600010=182

	
	/*
	# Subscriptions
	# BOOK TICKER Stream
	8=FIX.4.4|9=132|35=V|49=TRADER1|56=SPOT|34=4|52=20241122-06:17:14.183428|262=BOOK_TICKER_STREAM|263=1|264=1|266=Y|146=1|55=BTCUSDT|267=2|269=0|269=1|10=010|
	
	# DEPTH Stream
	8=FIX.4.4|9=127|35=V|49=TRADER1|56=SPOT|34=7|52=20241122-06:17:14.443822|262=DEPTH_STREAM|263=1|264=10|266=Y|146=1|55=BTCUSDT|267=2|269=0|269=1|10=111|
	*/
	public BinanceFixClient subscribeBook(String symbol) {
		FixMessageEncoder bookSub = session.newEncoder("V")
		.set(262, UUID.randomUUID().toString() )	// MDReqID
		.set(263, '1')	// SubscriptionRequestType
		.set(264, 1) // MarketDepth
		.set(146, 1) // NoRelatedSym
		.set(55, symbol)
		.set(267, 2) // NoMDEntryTypes
		.set(267, 269, '0') // bid
		.set(267, 269, '1') // offer
		;
		
		session.sendMessage(bookSub);
		return this;
	}
}
