package com.litefix.connectors.binance;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.connectors.AbstractConnector;
import com.litefix.connectors.Book;
import com.litefix.connectors.Book.BookLevel;
import com.litefix.connectors.Trade;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageDecoder.GroupDecoder;
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
public abstract class BinanceFixClient extends AbstractConnector {
	
	private ClientFixSession session;
	private PrivateKey privateKey;
	private String apiKey;
	
	// Symbol, Book
	Map<String,Book<BigDecimal,BigDecimal>> lastBookMap = new HashMap<>();
	// RequestId, 
	private Set<String> tradeSubsMap = new HashSet<>();
	
	public abstract void onMarketData( Book<BigDecimal,BigDecimal> m );
	
	public abstract void onMarketData( Trade t);
	
	
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
	
	@Override
	public void onMessageRcv(FixMessageDecoder decoder) throws BusinessRejectMessageException {
		switch(decoder.getMsgType()) {
			case "W" :
				onMarketData( decodeMarketDataSnapshot( decoder ) );
				break;
			case "X" :
				String requestId = decoder.asString(262); 	// MDReqID
				if ( this.tradeSubsMap.contains(requestId) ) {
					onMarketData( decodeBookIncrementalRefresh( requestId, decoder ) );
				} else {
					onMarketData( decodeTradeIncrementalRefresh( requestId, decoder ) );
				}
				break;
			default:
				System.out.println("Unsupported onMessageRcv() > "+decoder.getMsgType());
				break;
		}
	}
	
	// MarketDataSnapshot<W>
	// RCV: 8=FIX.4.49=000021335=W49=SPOT56=SPOTTEST34=252=20250130-18:59:55.088145
	// 262=338a6a9f-cd55-42fb-a7c0-85aae0602a6455=BTCUSDT25044=10762840268=2
	// 269=0270=105427.98000000271=0.00276000269=1270=105427.99000000271=0.0036600010=182
	Book<BigDecimal,BigDecimal> decodeMarketDataSnapshot(FixMessageDecoder decoder) {
		String symbol = decoder.asString(55);
		Book<BigDecimal,BigDecimal> m = this.lastBookMap.get(symbol);
		m.setRcvNanoTime(decoder.getRcvNanoTime());
		
		GroupDecoder levelsDecoder = decoder.asGroupDecoder(268); // NoMDEntries
		
		for ( int i=0; i<levelsDecoder.getGroupSize(); i++ ) {
			char side = levelsDecoder.at(i).asChar(269);
			BigDecimal price = levelsDecoder.at(i).asBigDecimal(270);	// MDEntryPx
			BigDecimal qty = levelsDecoder.at(i).asBigDecimal(271);		// MDEntrySize
			
			BookLevel<BigDecimal,BigDecimal> level = new BookLevel<>( price,qty );
			m.addLevel(level, side=='0');
		}		
		return m;
	}
	
	/*
	8=FIX.4.49=000017835=X49=SPOT56=SPOTTEST34=352=20250201-17:06:35.135896262=1636e032-9077-4fcf-8406-07e185c91187
	268=1279=1269=0270=102075.16000000271=0.0049000055=BTCUSDT25044=1211832410=222
	
	8=FIX.4.49=000017835=X49=SPOT56=SPOTTEST34=452=20250201-17:06:39.556023262=1636e032-9077-4fcf-8406-07e185c91187
	268=1279=1269=0270=102075.15000000271=0.0029000055=BTCUSDT25044=1211833110=211
	
	8=FIX.4.49=000017835=X49=SPOT56=SPOTTEST34=552=20250201-17:06:39.566566262=1636e032-9077-4fcf-8406-07e185c91187
	268=1279=1269=0270=102052.00000000271=0.0002300055=BTCUSDT25044=1211833210=209
	*/		
	Book<BigDecimal,BigDecimal> decodeBookIncrementalRefresh(String requestId, FixMessageDecoder decoder) {
		String symbol = decoder.asString(55);
		Book<BigDecimal,BigDecimal> cachedBook = lastBookMap.get(symbol);
		cachedBook.setRcvNanoTime(decoder.getRcvNanoTime());
		
		GroupDecoder levelsDecoder = decoder.asGroupDecoder(268); // NoMDEntries
		
		for ( int i=0; i<levelsDecoder.getGroupSize(); i++ ) {			
			char side = levelsDecoder.at(i).asChar(269);
			BigDecimal price = levelsDecoder.at(i).asBigDecimal(270);	// MDEntryPx
			char action = levelsDecoder.at(i).asChar(279); // MDUpdateAction
			
			// Del
			if ( action=='2' ) {
				if ( cachedBook.getDepth()==1 ) {
					if (!cachedBook.delBestLevel(side=='0')) {
						System.out.println("Cannot process DEL: no BEST level found");
					}
				} else {
					if (!cachedBook.delLevel(price, side=='0')) {
						System.out.println("Cannot process DEL: no level found for price:"+price.toPlainString());
					}
				}
				break;
			} else {
				BigDecimal qty = levelsDecoder.at(i).asBigDecimal(271);		// MDEntrySize
				switch (action) {
				case '0': // New
					if ( cachedBook.getDepth()==1 ) {
						cachedBook.setBestLevel( price, qty, side=='0');
					} else {
						cachedBook.addLevel( price, qty, side=='0');
					}
					break;
				case '1': // Upd
					int LastBookUpdateID = levelsDecoder.at(i).asInt(25044);	// LastBookUpdateID
					BookLevel<BigDecimal,BigDecimal> level = ( cachedBook.getDepth()==1 )?
						cachedBook.getBestLevel( side=='0'):cachedBook.findForPrice(price, side=='0');
					if ( level!=null ) {
						level.setSize(qty);
					} else {
						System.out.println("Cannot process UPD: no level found for price:"+price.toPlainString());
					}
					break;
				}
			}
		}
		return cachedBook;
	}
	
	/*
	8=FIX.4.49=000017735=X49=SPOT56=SPOTTEST34=352=20250202-13:00:40.676613262=af41c78b-b9c0-413e-b978-2606f855acda
	268=1279=1269=0270=98245.99000000271=0.0042800055=BTCUSDT25044=1271031010=246
	
	8=FIX.4.49=000017735=X49=SPOT56=SPOTTEST34=452=20250202-13:00:40.779251262=af41c78b-b9c0-413e-b978-2606f855acda
	268=1279=1269=1270=98839.28000000271=0.5094200055=BTCUSDT25044=1271031210=003
	 */
	Trade decodeTradeIncrementalRefresh(String requestId, FixMessageDecoder decoder) {
		String symbol = decoder.asString(55);
		
		Trade t = new Trade(requestId, symbol);
		// TODO: code me
		return t;
	}
	
	/*
	# Subscriptions
	# BOOK TICKER Stream
	8=FIX.4.4|9=132|35=V|49=TRADER1|56=SPOT|34=4|52=20241122-06:17:14.183428|262=BOOK_TICKER_STREAM|263=1|264=1|266=Y|146=1|55=BTCUSDT|267=2|269=0|269=1|10=010|
	
	# DEPTH Stream
	8=FIX.4.4|9=127|35=V|49=TRADER1|56=SPOT|34=7|52=20241122-06:17:14.443822|262=DEPTH_STREAM|263=1|264=10|266=Y|146=1|55=BTCUSDT|267=2|269=0|269=1|10=111|
	*/
	public BinanceFixClient subscribeBook(String symbol, String requestId, int bookLevels) {		
		FixMessageEncoder bookSub = session.newEncoder("V")
		.set(262, requestId )	// MDReqID
		.set(263, '1')	// SubscriptionRequestType
		.set(264, bookLevels) // MarketDepth
					 // 1 - Book Ticker subscription
					 // 2-5000 - Diff. Depth Stream
		.set(146, 1) // NoRelatedSym
		.set(55, symbol)
		.set(267, 2) // NoMDEntryTypes
		.set(267, 269, '0') // bid
		.set(267, 269, '1') // offer
		;
		
		this.tradeSubsMap.add(requestId);
		
		session.sendMessage(bookSub);
		
		// Send back existing image...if present
		Book<BigDecimal,BigDecimal> lastBook = this.lastBookMap.get(symbol);		
		if ( lastBook!=null ) {
			onMarketData(lastBook);
		} else {
			this.lastBookMap.put(symbol, 
				new Book<BigDecimal,BigDecimal>(
					requestId
				,	symbol	// Symbol
				,	null
				, bookLevels)
			);
		}
		return this;
	}
	
	public BinanceFixClient subscribeTrades(String symbol, String requestId) {		
		FixMessageEncoder bookSub = session.newEncoder("V")
		.set(262, requestId )	// MDReqID
		.set(263, '1')	// SubscriptionRequestType
		.set(264, 1) // MarketDepth
		.set(146, 1) // NoRelatedSym
		.set(55, symbol)
		.set(267, 1) // NoMDEntryTypes
		.set(267, 269, '2') // trades
		;
		session.sendMessage(bookSub);
		
		// Send back existing image...if present
		Book<BigDecimal,BigDecimal> lastBook = lastBookMap.get(symbol);		
		if ( lastBook!=null ) {
			onMarketData(lastBook);
		}
		return this;
	}
}
