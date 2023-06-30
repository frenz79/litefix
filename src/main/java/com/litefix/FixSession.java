package com.litefix;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;
import com.litefix.models.MsgType;
import com.litefix.modules.IFixMessagePool;
import com.litefix.modules.IMessagesDispatcher;
import com.litefix.modules.ITransport;
import com.litefix.modules.impl.AsyncMessagesDispatcher;
import com.litefix.modules.impl.FixMessagePool;
import com.litefix.modules.impl.FixMessageValidator;
import com.litefix.warmup.ArrayUtilsWarmup;
import com.litefix.warmup.FixMessageWarmup;
import com.litefix.warmup.MathUtilsWarmup;
import com.litefix.warmup.NumbersCacheWarmup;

public abstract class FixSession {

	public static interface FixSessionListener {
		
		public void onLogin();
		public void onLogout();
		public void onMessage(MsgType sgType, FixMessage msg) throws Exception;
		public void onConnection(boolean b);
		
	}
	
	static enum Status {
		KO,
		PENDING,
		OK		
	};

	// 20221216-12:21:31.683
	private static final DateTimeFormatter SENDING_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
	
	private String beginString;
	protected String senderCompId;
	protected String targetCompId;
	
	protected int hbIntervalSec;
	protected boolean resetSeqOnLogon;
	private long testRequestTolerance = 1000L;
			
	protected Status sessionStatus = Status.KO;
	private Status testRequestStatus = Status.KO;
	
	private long lastRcvTime = 0l;
	private long lastSndTime = 0l;
	private int outgoingSeq = 0;
	
	protected IFixMessagePool messageFactory;
	protected ITransport transport;
	protected IMessagesDispatcher messagesDispatcher;
	protected FixMessageValidator messageValidator;
	
	protected final FixSessionListener fixSessionListener;
	
	public FixSession( FixSessionListener fixSessionListener ) {
		super();
		this.fixSessionListener = fixSessionListener;
	}
	
	public FixSession withMessagesDispatcher(AsyncMessagesDispatcher messagesDispatcher) {
		this.messagesDispatcher = messagesDispatcher;
		return this;
	}
	
	public FixSession withMessagesValidator(FixMessageValidator messageValidator) {
		this.messageValidator = messageValidator;
		return this;
	}
	
	public FixSession withBeginString( String beginString ) {
		this.beginString = beginString;
		return this;
	}

	public FixSession withSenderCompId( String senderCompId ) {
		this.senderCompId = senderCompId;
		return this;
	}
	
	public FixSession withResetSeqOnLogon( boolean resetSeqOnLogon ) {
		this.resetSeqOnLogon = resetSeqOnLogon;
		return this;
	}
	
	public FixSession withHbIntervalSec( int hbIntervalSec ) {
		this.hbIntervalSec = hbIntervalSec;
		return this;
	}
	
	public FixSession withTargetCompId( String targetCompId ) {
		this.targetCompId = targetCompId;
		return this;
	}
	
	public FixSession withMessageFactory( FixMessagePool fixMessageFactory ) {
		this.messageFactory = fixMessageFactory;
		return this;
	}
	
	public FixSession withTransport( ITransport transport ) {
		this.transport = transport;
		return this;
	}
	
	public String getBeginString() {
		return beginString;
	}

	public String getSenderCompId() {
		return senderCompId;
	}

	public String getTargetCompId() {
		return targetCompId;
	}
	
	public FixSession send( FixMessage message ) throws Exception {
		if ( !sessionStatus.equals(Status.OK) && !message.getMsgType().is("A")) {
			throw new Exception("Not logged in");
		}

		message.build(
			beginString,
			senderCompId,
			targetCompId,				
			LocalDateTime.now(ZoneOffset.UTC).format( SENDING_TIME_FORMATTER ),
			++outgoingSeq
		);
		
		transport.send( message );
		this.lastSndTime = System.nanoTime();
		//System.out.println(">> outgoing: "+message);
		return this;
	}
	
	public FixSession sendReject(FixField refSeqNum, String text, MsgType refMsgType ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messageFactory.get().setMsgType("3")
				.addField( refSeqNum ) // TODO: change me
				.addField( new FixTag(372), refMsgType.getBytes() )
			;
			
			if ( text!=null && text.length()>0 ) {
				msg.addField( new FixTag(58), text );
			}
			
			send( msg ); 
			return this;
		} finally {
			messageFactory.release(msg);
		}
	}
	
	public FixSession doLogout( String reason ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messageFactory.get().setMsgType("5");
			if ( reason!=null && !reason.isEmpty() ) {
				msg.addField(new FixTag(58), reason);
			}
			send( msg );
			this.sessionStatus = Status.KO;
			return this;
		} finally {
			messageFactory.release(msg);
		}
	}	
	
	private FixSession sendHeartbeat( FixField testReqId ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messageFactory.get().setMsgType("0");
			if ( testReqId!=null ) {
				msg.addField( testReqId );
			}
			send( msg );
			return this;
		} finally {
			messageFactory.release(msg);
		}
	}
	
	private FixSession sendTestRequest() throws Exception {
		FixMessage msg = null;
		try {
			msg = messageFactory.get().setMsgType("1")
				.addField( new FixTag(112), UUID.randomUUID().toString() );
			send( msg );
			return this;
		} finally {
			messageFactory.release(msg);
		}
	}
	
	protected void runLoop() throws Exception {
		FixMessage msg = this.messageFactory.get();
		byte[] beginMessageDelimiter = ("8="+beginString+"9=").getBytes();
		
		messageValidator.setSenderCompId(senderCompId);
		messageValidator.setTargetCompId(targetCompId);
		
		while ( true ) {
			msg.reset();
			long now = System.nanoTime();
			
			if ( this.transport.pollMessage( msg.getBuffer(), beginMessageDelimiter ) ) {				
				this.lastRcvTime = now;
				
				if (!handleFixMessage( msg, now ) ) {
					msg = this.messageFactory.get();
					msg.reset();
				}
			}
			
			if ( Status.OK.equals( this.sessionStatus ) ) {
				if (!Status.PENDING.equals( this.testRequestStatus ) && now - this.lastRcvTime > (this.hbIntervalSec*1_000_000_000L+testRequestTolerance) ) {
					sendTestRequest();
					this.testRequestStatus = Status.PENDING;
				}
				if (now - this.lastSndTime > this.hbIntervalSec*1_000_000_000L ) {
					sendHeartbeat( null );
				}
			}
		}
	}
	
	private boolean handleFixMessage( FixMessage msg, long now ) throws Exception {
		if ( !messageValidator.isValid(msg) ) {
			// TODO:...
			System.out.println("Discarded!");
		} else {
			long delta = TimeUnit.NANOSECONDS.toMicros(System.nanoTime()-now);
			System.out.println("<< incoming processed in ["+delta+"]micros : "+msg); 
			
			switch(msg.getMsgType().toString()) {
				case "A": // Logon
					this.sessionStatus = Status.OK;
					fixSessionListener.onLogin();
					break;
				case "0": // heartbeat					
					this.testRequestStatus = Status.OK; // TODO: check field 112
					break;
				case "1": // test request					
					sendHeartbeat( msg.getField( new FixTag(112), new FixField() ) );
					break;
				case "2":
					// Resend Request
					break;
				case "3":
					// Reject
					break;
				case "4":
					// Sequence Reset
					break;					
				case "5": // logout
					this.sessionStatus = Status.KO;
					fixSessionListener.onLogout();
					break;
				default:
					if ( messagesDispatcher==null ) {
						dispatchMessage( msg );
					} else {
						messagesDispatcher.onAsyncMessage( msg, this );						
					}
					return false;			
			}
		}
		return true;
	}
	
	public void dispatchMessage( FixMessage msg ) {
		try {
			fixSessionListener.onMessage( msg.getMsgType(), msg );
		} catch ( Exception ex ) {
			try {
				sendReject( msg.getField( FixTag.HEADER_TAG_SEQ_NUM, new FixField()), ex.getMessage(), msg.getMsgType() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			messageFactory.release(msg);
		}
	}

	public IFixMessagePool getMessageFactory() {
		return messageFactory;
	}

	public FixSession doWarmup(int iterations) {
		ArrayUtilsWarmup.warmup(iterations);
		NumbersCacheWarmup.warmup(iterations);
		MathUtilsWarmup.warmup(iterations);
		FixMessageWarmup.warmup(iterations);
		return this;
	}
	
}
