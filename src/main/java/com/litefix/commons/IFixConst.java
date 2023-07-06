package com.litefix.commons;

import com.litefix.models.FixTag;
import com.litefix.modules.impl.FixMessageValidator;

public interface IFixConst {
	
	public static final byte FIELD_SEPARATOR = '\001';
	public static final String BEGIN_STRING_FIX44 = "FIX.4.4";


	public static int DEFAULT_LOGON_TIMEOUT_SEC = 5;
	public static int DEFAULT_HB_INTERVAL_SEC = 5;
	public static boolean DEFAULT_RESET_ON_LOGON = true;
	public static boolean DEFAULT_RESET_ON_DISCONNECT = true;
	public static boolean DEFAULT_IGNORE_SEQ_NUM_TOO_LOW_AT_LOGON = false;
	public static boolean DEFAULT_AUTOMATIC_LOGON = true;
	public static boolean DEFAULT_AUTOMATIC_RECONNECT = true;	
	public static int DEFAULT_MSG_VALIDATOR_FLAGS = FixMessageValidator.CRC;
	
	public static class StandardHeader {
		public static final FixTag BeginString = new FixTag(8);
		public static final FixTag BodyLen = new FixTag(9);
		public static final FixTag MsgType = new FixTag(35);
		public static final FixTag SenderCompID = new FixTag(49);
		public static final FixTag TargetCompID = new FixTag(56);
		public static final FixTag OnBehalfOfCompID = new FixTag(115);
		public static final FixTag DeliverToCompID = new FixTag(128);
		public static final FixTag SecureDataLen = new FixTag(90);
		public static final FixTag SecureData = new FixTag(91);	
		public static final FixTag MsgSeqNum = new FixTag(34);
		public static final FixTag SenderSubID = new FixTag(50);
		public static final FixTag SenderLocationID = new FixTag(142);	
		public static final FixTag TargetSubID = new FixTag(57);
		public static final FixTag TargetLocationID = new FixTag(143);
		public static final FixTag OnBehalfOfSubID = new FixTag(116);
		public static final FixTag OnBehalfOfLocationID = new FixTag(144);
		public static final FixTag DeliverToSubID = new FixTag(129);
		public static final FixTag DeliverToLocationID = new FixTag(145);
		public static final FixTag PossDupFlag = new FixTag(43); 
		public static final FixTag PossResend = new FixTag(97);
		public static final FixTag SendingTime = new FixTag(52);
		public static final FixTag OrigSendingTime = new FixTag(122);
		public static final FixTag XmlDataLen = new FixTag(212);	
		public static final FixTag XmlData = new FixTag(213);
		public static final FixTag MessageEncoding = new FixTag(347);	
		public static final FixTag LastMsgSeqNumProcessed = new FixTag(369);
		public static final FixTag NoHops = new FixTag(627);	
		public static final FixTag HopCompID = new FixTag(628);
		public static final FixTag HopSendingTime = new FixTag(629);
		public static final FixTag HopRefID = new FixTag(630);	
	}

	public static class StandardTrailer {
		public static final FixTag SignatureLength = new FixTag(93);
		public static final FixTag Signature = new FixTag(89);	
		public static final FixTag CheckSum = new FixTag(10);
	}

	public static class Logon {
		public static final FixTag EncryptMethod = new FixTag(98);
		public static final FixTag HeartBtInt = new FixTag(108);
		public static final FixTag RawDataLength = new FixTag(95);
		public static final FixTag RawData = new FixTag(96);
		public static final FixTag ResetSeqNumFlag = new FixTag(141);  
		public static final FixTag NextExpectedMsgSeqNum = new FixTag(789);
		public static final FixTag MaxMessageSize = new FixTag(383);
		public static final FixTag NoMsgTypes = new FixTag(384);
		public static final FixTag RefMsgType = new FixTag(372);
		public static final FixTag MsgDirection = new FixTag(385);
		public static final FixTag TestMessageIndicator = new FixTag(464);
		public static final FixTag Username = new FixTag(553);
		public static final FixTag Password = new FixTag(554);
	}
	
	public static class Logout {
		public static final FixTag Text = new FixTag(58);
		public static final FixTag EncodedTextLen = new FixTag(354);
		public static final FixTag EncodedText = new FixTag(355);
	}
		
	public static class ResendRequest {
		public static final FixTag BeginSeqNo = new FixTag(7); 
		public static final FixTag EndSeqNo = new FixTag(16);  
		public static final FixTag NewSeqNo = new FixTag(36);
	}

	public static class SequenceReset {
		public static final FixTag NewSeqNo = new FixTag(36); 
		public static final FixTag GapFillFlag = new FixTag(123);
	}
	
	public static class Heartbeat {
		public static final FixTag TestReqID = new FixTag(112);  
	}

	public static class TestRequest {
		public static final FixTag TestReqID = new FixTag(112);  
	}
	
	public static class Reject {
		public static final FixTag RefSeqNum = new FixTag(45); 
		public static final FixTag RefTagID = new FixTag(371); 
		public static final FixTag RefMsgType = new FixTag(372); 
		public static final FixTag SessionRejectReason = new FixTag(373); 
		public static final FixTag Text = new FixTag(58); 
		public static final FixTag EncodedTextLen = new FixTag(354); 
		public static final FixTag EncodedText = new FixTag(355); 
	}
	
	public static class BusinessMessageReject {
		public static final FixTag RefSeqNum = new FixTag(45);
		public static final FixTag RefMsgType = new FixTag(372); 
		public static final FixTag BusinessRejectRefID = new FixTag(379);
		public static final FixTag BusinessRejectReason = new FixTag(379);		
		public static final FixTag Text = new FixTag(58); 
		public static final FixTag EncodedTextLen = new FixTag(354); 
		public static final FixTag EncodedText = new FixTag(355); 
	}	
	
	public static final FixTag Symbol = new FixTag(55); 
	public static final FixTag QuoteID = new FixTag(117); 
	public static final FixTag ClOrdID = new FixTag(11); 	
	public static final FixTag Currency = new FixTag(15); 
	public static final FixTag HandlInst = new FixTag(21); 
	public static final FixTag OrderQty = new FixTag(38); 
	public static final FixTag OrdType = new FixTag(40); 
	public static final FixTag Side = new FixTag(54); 
	public static final FixTag TransactTime = new FixTag(60);
}
