package com.litefix;

import com.litefix.commons.IFixConst;
import com.litefix.models.FixField;
import com.litefix.models.FixMessage;
import com.litefix.modules.IPersistence;

public class FixSessionMessagesHandler {
	
	private final FixSession session;
	private final IPersistence persistence;
	private final FixSessionMessagesSender msgSender;
	
	public FixSessionMessagesHandler(FixSession session, IPersistence persistence, FixSessionMessagesSender msgSender) {
		super();
		this.session = session;
		this.persistence = persistence;
		this.msgSender = msgSender;
	}
	
	boolean processResendRequest( FixMessage msg ) {
		try {
			FixField beginSeqNo = msg.getField( IFixConst.BeginSeqNo );
			FixField endSeqNo = msg.getField( IFixConst.EndSeqNo );
			int maxEndSeqIdx = this.persistence.getLastOutgoingSeq();			
			int endIdx = (endSeqNo.is("0"))?maxEndSeqIdx:endSeqNo.valueAsInt();
			
			if ( endIdx>maxEndSeqIdx ) {
				this.msgSender.sendReject( 
					msg.getField( IFixConst.SeqNum ), 
					String.format("Invalid Resend Request: BeginSeqNo (%d) is greater than expected (%d).",beginSeqNo, endSeqNo),
					msg.getMsgType().toString()
				);
				return false;
			}
			if ( endIdx>2147483647 ) {
				this.msgSender.sendReject( 
					msg.getField( IFixConst.SeqNum ), 
					"nvalid Resend Request: BeginSeqNo <= 0.",
					msg.getMsgType().toString()
				);
				return false;
			}
			
			int startIdx = beginSeqNo.valueAsInt();
			
			for ( int i=startIdx; i<=endIdx; i++) {
				FixMessage dupMsg = this.persistence.findOutgoingMessageBySeq( i );
				if ( dupMsg!=null && !dupMsg.getMsgType().in("A", "5", "2", "0", "1", "4")) {
					this.session.send(dupMsg, true);
				}
			}
			return true;
		} catch ( Exception ex ) {
			ex.printStackTrace();
			try {
				this.msgSender.sendReject( msg.getField( IFixConst.SeqNum ), ex.getMessage(), msg.getMsgType().toString() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	boolean processGapFillRequest( FixMessage msg ) {
		try {
			int lastIncomingSeq = this.persistence.getLastIncomingSeq();
			int seqNum = msg.getField(IFixConst.SeqNum).valueAsInt();
			int newSeqNo = msg.getField(IFixConst.NewSeqNo).valueAsInt();
			
			if ( newSeqNo<this.persistence.getLastOutgoingSeq() ) {						
				this.msgSender.sendReject( 
					msg.getField( IFixConst.SeqNum ), 
					String.format("NewSeqNo too small expected %d, received %d", lastIncomingSeq+1, newSeqNo), 
					"4"
				);
				return false;
			}
			
			int startIdx = seqNum;
			int endIdx = newSeqNo;
			
			for ( int i=startIdx; i<=endIdx; i++) {
				FixMessage dupMsg = this.persistence.findOutgoingMessageBySeq( i );
				if ( dupMsg!=null && !dupMsg.getMsgType().in("A", "5", "2", "0", "1", "4")) {
					this.session.send(dupMsg, true);
				}
			}
			
			return true;
		} catch( Exception ex ) {
			ex.printStackTrace( );
			try {
				this.msgSender.sendReject( msg.getField( IFixConst.SeqNum ), ex.getMessage(), "4" );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	boolean processLogonResp( FixMessage msg ) {
		try {
			int lastIncomingSeq = this.persistence.getLastIncomingSeq();
			int seqNum = msg.getField(IFixConst.SeqNum).valueAsInt();	
			
			if ( !this.session.ignoreSeqNumTooLowAtLogon ) {						
				int delta = seqNum - lastIncomingSeq;
				if ( delta>1 ) {					
					this.msgSender.sendReject( 
						msg.getField( IFixConst.SeqNum ), 
						String.format("Incoming seq too small in Logon(A) message, expected %d, received %d", lastIncomingSeq+1, seqNum), 
						"A"
					);
					return false;
				}
			}
			
			FixField NextExpectedMsgSeqNum = msg.getField(IFixConst.NextExpectedMsgSeqNum);
			if ( NextExpectedMsgSeqNum!=null ) {
				if ( seqNum!=lastIncomingSeq+1 ) {
					this.msgSender.sendGapFillRequest( seqNum, lastIncomingSeq+1 );
				}
			}
			
			return true;
		} catch( Exception ex ) {
			ex.printStackTrace( );
			try {
				this.msgSender.sendReject( msg.getField( IFixConst.SeqNum ), ex.getMessage(), "A" );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
