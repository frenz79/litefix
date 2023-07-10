package com.litefix;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.IFixConst;
import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.BusinessRejectMessageException.BUSINESS_REJECT_REASON;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.commons.utils.FixUUID;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;
import com.litefix.modules.IFixMessagePool;

public class FixSessionMsgSender {

	private final IFixMessagePool messagePool;
	private final FixSession session;
		
	public FixSessionMsgSender(FixSession session, IFixMessagePool messagePool) {
		super();
		this.messagePool = messagePool;
		this.session = session;
	}

	boolean sendHeartbeat( FixField testReqId ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("0");
			if ( testReqId!=null ) {
				msg.addField( testReqId );
			}
			session.send( msg );
			return true;
		} finally {
			messagePool.release(msg);
		}
	}
	
	boolean sendTestRequest() throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("1")
				.addField(IFixConst.TestRequest.TestReqID, FixUUID.random() );
			session.send( msg );
			return true;
		} finally {
			messagePool.release(msg);
		}
	}
	
	boolean sendSessionReject( SessionRejectMessageException ex ) throws Exception {
		return sendSessionReject(ex.getRefSeqNum(), ex.getText(), ex.getRefMsgType(), ex.getSessionRejectReason() );
	}
	
	boolean sendSessionReject(int refSeqNum, String text, String refMsgType) throws Exception {
		return sendSessionReject(refSeqNum, text, refMsgType, null);
	}
	
	boolean sendSessionReject(int refSeqNum, String text, String refMsgType, SESSION_REJECT_REASON reasonId ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("3")
				.addField( IFixConst.Reject.RefSeqNum, NumbersCache.toString(refSeqNum) )
				.addField( IFixConst.Reject.RefMsgType, refMsgType )
				.addField( IFixConst.Reject.SessionRejectReason, NumbersCache.toString( 
						((reasonId!=null)?reasonId:SESSION_REJECT_REASON.OTHER).getValue() ))
			;
			
			if ( text!=null && text.length()>0 ) {
				msg.addField( IFixConst.Reject.Text, text );
			}			
			session.send( msg ); 
			return true;
		} finally {
			messagePool.release(msg);
		}
	}
	
	public void sendBusinessReject( BusinessRejectMessageException ex) throws Exception {
		sendBusinessReject(ex.getRefSeqNum(), ex.getText(), ex.getRefMsgType(), ex.getBusinessRejectReason() );
	}
	
	boolean sendBusinessReject(int refSeqNum, String text, String refMsgType, BUSINESS_REJECT_REASON reasonId) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("j")
				.addField( IFixConst.BusinessMessageReject.RefSeqNum, NumbersCache.toString(refSeqNum) )
				.addField( IFixConst.BusinessMessageReject.RefMsgType, refMsgType )
				.addField( IFixConst.BusinessMessageReject.BusinessRejectRefID, refMsgType )
				.addField( IFixConst.BusinessMessageReject.BusinessRejectReason, NumbersCache.toString( 
						((reasonId!=null)?reasonId:BUSINESS_REJECT_REASON.APPLICATION_NOT_AVAILABLE).getValue() ))
			;			
			if ( text!=null && text.length()>0 ) {
				msg.addField( IFixConst.Reject.Text, text );
			}			
			session.send( msg ); 
			return true;
		} finally {
			messagePool.release(msg);
		}
	}
	
	boolean sendGapFillRequest( int BeginSeqNo, int EndSeqNo ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType(MsgType.TAG.GAP_FILL.getValue())
				.addField( IFixConst.SequenceReset.GapFillFlag, "Y" ) 
				.addField( IFixConst.SequenceReset.NewSeqNo, EndSeqNo ) 
			;
			session.send( msg, BeginSeqNo );
			return true;
		} finally {
			messagePool.release(msg);
		}
	}
	
}
