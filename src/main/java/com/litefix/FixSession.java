package com.litefix;

import com.litefix.commons.IFixConst;
import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.InvalidSessionException;
import com.litefix.commons.exceptions.InvalidSessionException.CAUSE;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.StringUtils;
import com.litefix.commons.utils.TimeUtils;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;
import com.litefix.models.MsgType.TAG;
import com.litefix.models.SessionStateMachine;
import com.litefix.modules.IFixMessagePool;
import com.litefix.modules.IFixMessageValidator;
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

	// Mandatory fields to be set
	String beginString;
	String senderCompId;
	String targetCompId;

	// Optional fields with default values
	protected boolean automaticLogonOnConnect = IFixConst.DEFAULT_AUTOMATIC_LOGON;
	protected boolean automaticLogonOnLogout = IFixConst.DEFAULT_AUTOMATIC_LOGON;	
	protected boolean automaticReconnect = IFixConst.DEFAULT_AUTOMATIC_RECONNECT;
	protected long automaticReconnectRetryDelayMillis = 100L;
	protected int logonTimeoutSec = IFixConst.DEFAULT_LOGON_TIMEOUT_SEC;
	protected int hbIntervalSec = IFixConst.DEFAULT_HB_INTERVAL_SEC;
	protected boolean resetSeqOnLogon = IFixConst.DEFAULT_RESET_ON_LOGON;
	protected boolean resetSeqOnDisconnect = IFixConst.DEFAULT_RESET_ON_DISCONNECT;
	protected boolean ignoreSeqNumTooLowAtLogon = IFixConst.DEFAULT_IGNORE_SEQ_NUM_TOO_LOW_AT_LOGON;

	private long testRequestTolerance = 1000L;

	protected FixSessionMessagesSender sessionMessagesSender;
	protected FixSessionMessagesHandler sessionMessageHanlder;

	protected IPersistence<FixMessage> persistence;
	protected IFixMessagePool messagePool;
	protected ITransport transport;
	protected IMessagesDispatcher messagesDispatcher;
	protected IFixMessageValidator messageValidator;

	protected final SessionStateMachine stateMachine;

	protected final IFixSessionListener fixSessionListener;

	public FixSession( IFixSessionListener fixSessionListener ) {
		super();
		this.fixSessionListener = fixSessionListener;
		this.stateMachine = new SessionStateMachine();
	}

	/* Called in a separate thread
	 * 
	 */
	void messagePoller() throws Exception {
		FixMessage msg = this.messagePool.get();
		byte[] beginMessageDelimiter = ("8="+beginString+"9=").getBytes();
		while ( true ) {
			msg.reset();
			long now = System.nanoTime();

			if ( this.transport.pollMessage( msg.getBuffer(), beginMessageDelimiter ) ) {				
				if (!handleFixMessage( msg, now ) ) {
					msg = this.messagePool.get();
					msg.reset();
				}
			}			
			doCiclycTasks( now );
		}
	}

	void doCiclycTasks( long now ) throws Exception {
		// Check for session timeouts
		if ( this.stateMachine.isLoggedOn() ) {
			long hbIntervalSecWithTol = this.hbIntervalSec*1_000_000_000L+testRequestTolerance;
					
			if (( now - this.stateMachine.getLastMessageReceivedNanos())> hbIntervalSecWithTol) {
				if ( ( now - this.stateMachine.getLastTestRequestSentNanos() )> hbIntervalSecWithTol) {
					throw new Exception("Connection timedout");
				} else {
					this.stateMachine.setLastTestRequestSentNanos(now);
					sessionMessagesSender.sendTestRequest();
				}
			}
		
			// Send HB if necessary
			if (now - this.stateMachine.getLastMessageSentNanos() > this.hbIntervalSec*1_000_000_000L ) {
				sessionMessagesSender.sendHeartbeat( null );
			}
		}
	}

	private void handleLoginProcess( TAG msgType, FixMessage msg ) throws SessionRejectMessageException, BusinessRejectMessageException {		
		if ( this.stateMachine.isWaitingForLoginResp( )) {
			switch(msgType) {
			case LOGON:
				boolean logonResult = sessionMessageHanlder.processLogonResp( msg );
				this.stateMachine.logon(logonResult);
				fixSessionListener.onLogin(msg, logonResult);
				break;
			case LOGOUT:
				this.stateMachine.logon(false);
				fixSessionListener.onLogout(msg);
				break;
			default:
				System.out.println("Discarded message received while not loggedin:"+msg);
				throw new SessionRejectMessageException(
						msg.getHederField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(),
						35,
						msgType.getValue(),
						SESSION_REJECT_REASON.INVALID_MSGTYPE_35,
						String.format("Invalid messageType %s received", msgType.getValue())	
				);
			}
		}
	}

	private boolean handleFixMessage( FixMessage msg, long rcvTime ) throws Exception {
		this.stateMachine.setLastMessageReceivedNanos(rcvTime);
		TAG msgType = msg.getMsgType().getTag();
		int msgSeqNum = msg.getHederField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt();
		int expMsgSeqNum = this.persistence.getLastIncomingSeq() + 1;
		
		try {
			messageValidator.validate(msg);			
		} catch (SessionRejectMessageException ex1) {
			ex1.printStackTrace();
			
			sessionMessagesSender.sendSessionReject( ex1 );
			
			if ( !this.stateMachine.isLoggedOn() ) { 
				doLogout("Session error");
			}
			
		} catch (Exception ex3) {
			ex3.printStackTrace();
			sessionMessagesSender.sendSessionReject( 
					msgSeqNum, 
					ex3.getMessage(), 
					msgType.toString()
					);
		}

		//long validationTime = TimeUnit.NANOSECONDS.toMicros(System.nanoTime()-rcvTime);
		System.out.println("<< incoming: "+msg); 

		// Gap detection
		if ( msgSeqNum != expMsgSeqNum ) {
			System.out.println("Gap detected, received "+msgSeqNum+" but expected "+expMsgSeqNum);
			// TODO: code me

			return true;
		}
		this.persistence.setLastIncomingSeq(msgSeqNum);

		try  {
			if ( !this.stateMachine.isLoggedOn() ) {
				handleLoginProcess( msgType, msg );
			} else {
				switch(msgType) {
				case HEARTBEAT:					
					// NOP
					break;
				case TEST_REQUEST: 				
					sessionMessagesSender.sendHeartbeat( msg.getField( IFixConst.Heartbeat.TestReqID ) );
					break;
				case RESEND_REQUEST:
					this.stateMachine.setResending( true );
					sessionMessageHanlder.processResendRequest( msg );
					this.stateMachine.setResending( false );
					break;
				case GAP_FILL:
					this.stateMachine.setResending( true );
					sessionMessageHanlder.processGapFillRequest( msg );
					this.stateMachine.setResending( false );
					break;					
				case LOGOUT:
					this.stateMachine.logon(false);
					fixSessionListener.onLogout(msg);
					break;
				default:
					messagesDispatcher.dispatch( msg, this );						
					return false;
				}
			}
		} catch (SessionRejectMessageException ex1) {
			sessionMessagesSender.sendSessionReject( ex1 );
			
			if ( !this.stateMachine.isLoggedOn() ) { 
				doLogout("Session error");
			}
			
		} catch( BusinessRejectMessageException ex2 ) {
			sessionMessagesSender.sendBusinessReject( ex2 );
		} catch (Exception ex3) {
			sessionMessagesSender.sendSessionReject( 
					msgSeqNum, 
					ex3.getMessage(), 
					msgType.toString()
					);
		}

		return true;
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
		if ( !this.stateMachine.isLoggedOn() && !message.getMsgType().is("A")) {
			throw new Exception("Not logged in");
		}

		long now = System.nanoTime();

		if ( isDup ) {
			message.getField(IFixConst.StandardHeader.PossDupFlag).set("Y");
			// OrigSendingTime(122) = SendingTime
			message.getField(IFixConst.StandardHeader.SendingTime).set( TimeUtils.getSendingTime() );
			message.getField(IFixConst.StandardTrailer.CheckSum).set( message.calcChecksum() );			
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
		this.stateMachine.setLastMessageSentNanos(now);
		System.out.println(">> outgoing: "+message);
		return this;
	}

	public FixSession doLogout( String reason ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("5");
			if ( reason!=null && !reason.isEmpty() ) {
				msg.addField(IFixConst.Logout.Text, reason);
			}
			send( msg );
			this.stateMachine.loggedOut();
			return this;
		} finally {
			messagePool.release(msg);
		}
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
				sessionMessagesSender.sendSessionReject( 
					msg.getField( IFixConst.StandardHeader.MsgSeqNum ).valueAsInt(), ex.getMessage(), msgType.toString() );
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
			this.messageValidator = new FixMessageValidator(senderCompId, targetCompId, IFixConst.DEFAULT_MSG_VALIDATOR_FLAGS);
		}
		if ( this.persistence==null ) {
			this.persistence = new InMemoryPersistence<>(beginString,senderCompId,targetCompId);
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

	public FixSessionMessagesHandler getSessionMessageHanlder() {
		return sessionMessageHanlder;
	}
}
