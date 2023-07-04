package com.litefix;

import java.util.concurrent.TimeUnit;

import com.litefix.commons.IFixConst;
import com.litefix.commons.exceptions.InvalidSessionException;
import com.litefix.commons.exceptions.InvalidSessionException.CAUSE;
import com.litefix.commons.utils.StringUtils;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;
import com.litefix.models.MsgType.TAG;
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

	public static int DEFAULT_LOGON_TIMEOUT_SEC = 5;
	public static int DEFAULT_HB_INTERVAL_SEC = 5;
	public static boolean DEFAULT_RESET_ON_LOGON = true;
	public static boolean DEFAULT_RESET_ON_DISCONNECT = true;
	public static boolean DEFAULT_IGNORE_SEQ_NUM_TOO_LOW_AT_LOGON = false;
	public static boolean DEFAULT_AUTOMATIC_LOGON = true;
	public static boolean DEFAULT_AUTOMATIC_RECONNECT = true;	
	public static int DEFAULT_MSG_VALIDATOR_FLAGS = FixMessageValidator.CRC;

	// Mandatory fields to be set
	String beginString;
	String senderCompId;
	String targetCompId;

	// Optional fields with default values
	protected boolean automaticLogonOnConnect = DEFAULT_AUTOMATIC_LOGON;
	protected boolean automaticLogonOnLogout = DEFAULT_AUTOMATIC_LOGON;	
	protected boolean automaticReconnect = DEFAULT_AUTOMATIC_RECONNECT;
	protected long automaticReconnectRetryDelayMillis = 100L;
	protected int logonTimeoutSec = DEFAULT_LOGON_TIMEOUT_SEC;
	protected int hbIntervalSec = DEFAULT_HB_INTERVAL_SEC;
	protected boolean resetSeqOnLogon = DEFAULT_RESET_ON_LOGON;
	protected boolean resetSeqOnDisconnect = DEFAULT_RESET_ON_DISCONNECT;
	protected boolean ignoreSeqNumTooLowAtLogon = DEFAULT_IGNORE_SEQ_NUM_TOO_LOW_AT_LOGON;

	private long testRequestTolerance = 1000L;

	protected FixSessionMessagesSender sessionMessagesSender;
	protected FixSessionMessagesHandler sessionMessageHanlder;

	protected IPersistence persistence;
	protected IFixMessagePool messagePool;
	protected ITransport transport;
	protected IMessagesDispatcher messagesDispatcher;
	protected FixMessageValidator messageValidator;

	protected SessionStatus sessionStatus = SessionStatus.DISCONNECTED;

	private long lastRcvTime = 0l;
	private long lastSndTime = 0l;

	protected final IFixSessionListener fixSessionListener;

	public FixSession( IFixSessionListener fixSessionListener ) {
		super();
		this.fixSessionListener = fixSessionListener;
	}

	public FixSession send( FixMessage message, int seqNumber ) throws Exception {
		return send( message, false, seqNumber );
	}

	public FixSession send( FixMessage message ) throws Exception {
		return send( message, false, -1 );
	}

	public FixSession send( FixMessage message, boolean isDup ) throws Exception {
		return send( message, isDup, -1 );
	}

	public FixSession send( FixMessage message, boolean isDup, int seqNumber ) throws Exception {
		if ( !sessionStatus.equals(SessionStatus.ACTIVE) && !message.getMsgType().is("A")) {
			throw new Exception("Not logged in");
		}
		if ( isDup ) {
			message.getField(IFixConst.PossDupFlag).set("Y");
			// OrigSendingTime(122) = SendingTime
			message.getField(IFixConst.SendingTime).set( TimeUtils.getSendingTime() );
			message.getField(IFixConst.BODY_TAG_CHECKSUM).set( message.calcChecksum() );			
		} else {
			int sequence = (seqNumber<0)?persistence.getAndIncrementOutgoingSeq():seqNumber;
			message.build(
					beginString,
					senderCompId,
					targetCompId,				
					TimeUtils.getSendingTime(),
					sequence
					);
			if (!message.getMsgType().in("A", "5", "2", "0", "1", "4")) {
				persistence.storeOutgoingMessage( sequence, message );
			}			
		}

		transport.send( message );
		this.lastSndTime = System.nanoTime();
		System.out.println(">> outgoing: "+message);
		return this;
	}

	public FixSession doLogout( String reason ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("5");
			if ( reason!=null && !reason.isEmpty() ) {
				msg.addField(IFixConst.TAG_58, reason);
			}
			send( msg );
			setSessionStatus( SessionStatus.LOGGED_OUT );
			return this;
		} finally {
			messagePool.release(msg);
		}
	}	

	void messagePoller() throws Exception {
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
					sessionMessagesSender.sendTestRequest();
					setSessionStatus( SessionStatus.ACTIVE_WAIT );
				}
				if (now - this.lastSndTime > this.hbIntervalSec*1_000_000_000L ) {
					sessionMessagesSender.sendHeartbeat( null );
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
			TAG msgType = msg.getMsgType().getTag();

			if (SessionStatus.LOGON_SENT.equals(sessionStatus)) {
				switch(msgType) {
				case LOGON:
					if ( sessionMessageHanlder.processLogonResp( msg ) ) {
						setSessionStatus( SessionStatus.ACTIVE );
						fixSessionListener.onLoginSuccess(msg);
					} else {
						fixSessionListener.onLoginFailed(msg);
					}
					break;
				case LOGOUT:
					setSessionStatus( SessionStatus.LOGGED_OUT );
					fixSessionListener.onLogout(msg);
					break;
				default:
					System.out.println("Discarded message received while not loggedin:"+msg);
					break;
				}
			} else if (SessionStatus.ACTIVE.equals(sessionStatus) || SessionStatus.ACTIVE_WAIT.equals(sessionStatus)) {
				switch(msgType) {
				case HEARTBEAT:					
					setSessionStatus( SessionStatus.ACTIVE ); // TODO: check field 112
					break;
				case TEST_REQUEST: 				
					sessionMessagesSender.sendHeartbeat( msg.getField( IFixConst.TestReqID ) );
					break;
				case RESEND_REQUEST:
					setSessionStatus( SessionStatus.ACTIVE_RESEND );
					sessionMessageHanlder.processResendRequest( msg );
					setSessionStatus( SessionStatus.ACTIVE );
					break;
				case GAP_FILL:
					setSessionStatus( SessionStatus.ACTIVE_RESEND );
					sessionMessageHanlder.processGapFillRequest( msg );
					setSessionStatus( SessionStatus.ACTIVE );
					break;					
				case LOGOUT: // logout
					setSessionStatus( SessionStatus.LOGGED_OUT );
					fixSessionListener.onLogout(msg);
					break;
				default:
					messagesDispatcher.dispatch( msg, this );						
					return false;			
				}
			} else {
				sessionMessagesSender.sendReject( 
						msg.getHederField( IFixConst.MsgSeqNum ), 
						"Cannot process message" , 
						msg.getMsgType().toString()
						);
			}
		}
		return true;
	}	

	/* IMEssageDispatcher
	 * 
	 * */
	@Override
	public void dispatch( FixMessage msg, FixSession s ) {
		MsgType msgType = msg.getMsgType();
		try {
			fixSessionListener.onMessage( msgType, msg );
		} catch ( Exception ex ) {
			try {
				sessionMessagesSender.sendReject( msg.getField( IFixConst.SeqNum ), ex.getMessage(), msgType.toString() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			messagePool.release(msg);
		}
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

		this.sessionMessagesSender = new FixSessionMessagesSender( this, messagePool );
		this.sessionMessageHanlder = new FixSessionMessagesHandler( this, persistence, this.sessionMessagesSender );

		return this;
	}

	public IFixMessagePool getMessageFactory() {
		return messagePool;
	}

	public IFixMessagePool getMessagePool() {
		return messagePool;
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


	public SessionStatus getSessionStatus() {
		return sessionStatus;
	}

	void setSessionStatus(SessionStatus sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public FixSessionMessagesHandler getSessionMessageHanlder() {
		return sessionMessageHanlder;
	}
}
