package com.litefix;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import com.litefix.commons.IFixConst;
import com.litefix.commons.exceptions.InvalidSessionException;
import com.litefix.commons.exceptions.InvalidSessionException.CAUSE;
import com.litefix.commons.utils.FixUUID;
import com.litefix.commons.utils.StringUtils;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;
import com.litefix.models.MsgType;
import com.litefix.models.SessionStatus;
import com.litefix.modules.IFixMessagePool;
import com.litefix.modules.IMessagesDispatcher;
import com.litefix.modules.IPersistence;
import com.litefix.modules.ITransport;
import com.litefix.modules.impl.DefaultFixMessagePool;
import com.litefix.modules.impl.FixMessageValidator;
import com.litefix.modules.impl.InMemoryPersistence;
import com.litefix.modules.impl.SocketTransport;
import com.litefix.warmup.ArrayUtilsWarmup;
import com.litefix.warmup.FixMessageWarmup;
import com.litefix.warmup.MathUtilsWarmup;
import com.litefix.warmup.NumbersCacheWarmup;

public abstract class FixSession implements IMessagesDispatcher {
	
	public static int DEFAULT_HB_INTERVAL_SEC = 5;
	public static boolean DEFAULT_RESET_ON_LOGON = true;
	public static int DEFAULT_MSG_VALIDATOR_FLAGS = FixMessageValidator.CRC;
	
	// 20221216-12:21:31.683
	private static final DateTimeFormatter SENDING_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
	
	// Mandatory fields to be set
	String beginString;
	String senderCompId;
	String targetCompId;
	
	// Optional fields with default values
	protected int hbIntervalSec = DEFAULT_HB_INTERVAL_SEC;
	protected boolean resetSeqOnLogon = DEFAULT_RESET_ON_LOGON;
	private long testRequestTolerance = 1000L;
	
	protected IPersistence persistence;
	protected IFixMessagePool messagePool;
	protected ITransport transport;
	protected IMessagesDispatcher messagesDispatcher;
	protected FixMessageValidator messageValidator;
	
	protected SessionStatus sessionStatus = SessionStatus.DISCONNECTED;
	
	public SessionStatus getSessionStatus() {
		return sessionStatus;
	}

	void setSessionStatus(SessionStatus sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	private long lastRcvTime = 0l;
	private long lastSndTime = 0l;
	
	protected final IFixSessionListener fixSessionListener;
	
	public FixSession( IFixSessionListener fixSessionListener ) {
		super();
		this.fixSessionListener = fixSessionListener;
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
		return send( message, false );
	}
	
	public FixSession send( FixMessage message, boolean isDup ) throws Exception {
		if ( !sessionStatus.equals(SessionStatus.ACTIVE) && !message.getMsgType().is("A")) {
			throw new Exception("Not logged in");
		}
		if ( isDup ) {
			message.addHeader(IFixConst.PossDupFlag, "Y");
		} else {
			int sequence = persistence.getAndIncrementSeq();
			message.build(
					beginString,
					senderCompId,
					targetCompId,				
					LocalDateTime.now(ZoneOffset.UTC).format( SENDING_TIME_FORMATTER ),
					sequence
				);			
			persistence.store( sequence, message );
		}
		
		transport.send( message );
		this.lastSndTime = System.nanoTime();
		System.out.println(">> outgoing: "+message);
		return this;
	}
	
	public FixSession sendReject(FixField refSeqNum, String text, MsgType refMsgType ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("3")
				.addField( refSeqNum ) // TODO: change me
				.addField( IFixConst.TAG_372, refMsgType.getBytes() )
			;
			
			if ( text!=null && text.length()>0 ) {
				msg.addField( IFixConst.TAG_58, text );
			}
			
			send( msg ); 
			return this;
		} finally {
			messagePool.release(msg);
		}
	}
	
	public FixSession doLogout( String reason ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("5");
			if ( reason!=null && !reason.isEmpty() ) {
				msg.addField(IFixConst.TAG_58, reason);
			}
			send( msg );
			this.sessionStatus = SessionStatus.LOGGED_OUT;
			return this;
		} finally {
			messagePool.release(msg);
		}
	}	
	
	private FixSession sendHeartbeat( FixField testReqId ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("0");
			if ( testReqId!=null ) {
				msg.addField( testReqId );
			}
			send( msg );
			return this;
		} finally {
			messagePool.release(msg);
		}
	}
	
	private FixSession sendTestRequest() throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("1")
				.addField(IFixConst.TAG_112, FixUUID.random() );
			send( msg );
			return this;
		} finally {
			messagePool.release(msg);
		}
	}
	
	protected void runLoop() throws Exception {
		FixMessage msg = this.messagePool.get();
		byte[] beginMessageDelimiter = ("8="+beginString+"9=").getBytes();
		
		messageValidator.setSenderCompId(senderCompId);
		messageValidator.setTargetCompId(targetCompId);
		
		while ( true ) {
			msg.reset();
			long now = System.nanoTime();
			
			if ( this.transport.pollMessage( msg.getBuffer(), beginMessageDelimiter ) ) {				
				this.lastRcvTime = now;
				
				if (!handleFixMessage( msg, now ) ) {
					msg = this.messagePool.get();
					msg.reset();
				}
			}
			
			if ( SessionStatus.ACTIVE.equals( this.sessionStatus ) ) {
				if (!SessionStatus.ACTIVE_WAIT.equals( this.sessionStatus ) && now - this.lastRcvTime > (this.hbIntervalSec*1_000_000_000L+testRequestTolerance) ) {
					sendTestRequest();
					this.sessionStatus = SessionStatus.ACTIVE_WAIT;
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
			String msgType = msg.getMsgType().toString();
						
			if (SessionStatus.LOGON_SENT.equals(sessionStatus)) {
				if ( "A".equals(msgType) ) {
					this.sessionStatus = SessionStatus.ACTIVE;
					fixSessionListener.onLogin();
				}
				// No other message types are expected here
			} else if (SessionStatus.ACTIVE.equals(sessionStatus) || SessionStatus.ACTIVE_WAIT.equals(sessionStatus)) {
				switch(msgType) {
				case "0": // heartbeat					
					this.sessionStatus = SessionStatus.ACTIVE; // TODO: check field 112
					break;
				case "1": // test request					
					sendHeartbeat( msg.getField( IFixConst.TAG_112 ) );
					break;
				case "2":
					// Resend Request
					FixField BeginSeqNo = msg.getField( IFixConst.BeginSeqNo );
					FixField EndSeqNo = msg.getField( IFixConst.EndSeqNo );
					processResendRequest( BeginSeqNo, EndSeqNo );
					break;
				case "3":
					// Reject
					break;
				case "4":
					// SequenceReset GapFill 
					
					
					break;					
				case "5": // logout
					this.sessionStatus = SessionStatus.LOGGED_OUT;
					fixSessionListener.onLogout();
					break;
				default:
					messagesDispatcher.dispatch( msg, this );						
					return false;			
				}
			}
		}
		return true;
	}
	
	void processResendRequest(FixField beginSeqNo, FixField endSeqNo) throws Exception {
		int endIdx = (!endSeqNo.is("0"))?endSeqNo.valueAsInt():persistence.getLastSeq();
		int startIdx = beginSeqNo.valueAsInt();
		
		for ( int i=startIdx; i<=endIdx; i++) {
			FixMessage msg = persistence.findMessageBySeq( i );
			if ( msg!=null ) {
				if (!msg.getMsgType().in("A", "5", "2", "0", "1", "4")) {
					send(msg, true);
				}
			}
		}
	}

	/* IMEssageDispatcher
	 * 
	 * */
	@Override
	public void dispatch( FixMessage msg, FixSession s ) {
		try {
			fixSessionListener.onMessage( msg.getMsgType(), msg );
		} catch ( Exception ex ) {
			try {
				sendReject( msg.getField( FixTag.HEADER_TAG_SEQ_NUM, new FixField()), ex.getMessage(), msg.getMsgType() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			messagePool.release(msg);
		}
	}

	public IFixMessagePool getMessageFactory() {
		return messagePool;
	}

	public FixSession doWarmup(int iterations) {
		ArrayUtilsWarmup.warmup(iterations);
		NumbersCacheWarmup.warmup(iterations);
		MathUtilsWarmup.warmup(iterations);
		FixMessageWarmup.warmup(iterations);
		return this;
	}

	public FixSession validate() throws InvalidSessionException {
		if ( StringUtils.isEmpty(senderCompId) ) {
			throw new InvalidSessionException(CAUSE.FIELD_NOT_SET,"senderCompId");
		}
		if ( StringUtils.isEmpty(targetCompId) ) {
			throw new InvalidSessionException(CAUSE.FIELD_NOT_SET,"targetCompId");
		}
		if ( StringUtils.isEmpty(beginString) ) {
			throw new InvalidSessionException(CAUSE.FIELD_NOT_SET,"beginString");
		}
		
		if ( this.messagePool==null ) {
			this.messagePool = new DefaultFixMessagePool();
		}
		if ( this.transport==null ) {
			this.transport = new SocketTransport();
		}
		if ( this.messagesDispatcher==null ) {
			this.messagesDispatcher = this;
		}
		if ( this.messageValidator==null ) {
			this.messageValidator = new FixMessageValidator(DEFAULT_MSG_VALIDATOR_FLAGS);
		}
		if ( this.persistence==null ) {
			this.persistence = new InMemoryPersistence(beginString,senderCompId,beginString);
		}
		return this;
	}
	
	public IFixMessagePool getMessagePool() {
		return messagePool;
	}
	
}
