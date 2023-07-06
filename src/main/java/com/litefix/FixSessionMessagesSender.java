package com.litefix;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.IFixConst;
import com.litefix.commons.utils.FixUUID;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.models.MsgType;
import com.litefix.modules.IFixMessagePool;

public class FixSessionMessagesSender {

	private final IFixMessagePool messagePool;
	private final FixSession session;
		
	public FixSessionMessagesSender(FixSession session, IFixMessagePool messagePool) {
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
	
	boolean sendReject(int refSeqNum, String text, String refMsgType ) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("3")
				.addField( IFixConst.Reject.RefSeqNum, NumbersCache.toString(refSeqNum) )
				.addField( IFixConst.Reject.RefMsgType, refMsgType )
				.addField( IFixConst.Reject.SessionRejectReason, "99" ) // 99 = Other
				.addField( IFixConst.Reject.Text, text )
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
	
	boolean sendBusinessReject(int refSeqNum, String text, String refMsgType) throws Exception {
		FixMessage msg = null;
		try {
			msg = messagePool.get().setMsgType("j")
				.addField( IFixConst.BusinessMessageReject.RefSeqNum, NumbersCache.toString(refSeqNum) )
				.addField( IFixConst.BusinessMessageReject.RefMsgType, refMsgType )
				.addField( IFixConst.BusinessMessageReject.BusinessRejectRefID, refMsgType )
				.addField( IFixConst.BusinessMessageReject.BusinessRejectReason, "4" ) // 4 = Application not available
				.addField( IFixConst.Reject.Text, text )
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
