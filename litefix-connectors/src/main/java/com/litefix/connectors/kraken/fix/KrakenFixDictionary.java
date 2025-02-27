package com.litefix.connectors.kraken.fix;

import com.litefix.models.fixmessage.FixMessageDictionary;
import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;
import com.litefix.models.fixmessage.FixMessageDictionary.FixMessageFieldTemplate;

public class KrakenFixDictionary {

	private static FixMessageDictionary dictionary;	
	
	public static FixMessageDictionary init() {
		dictionary = new FixMessageDictionary("FIX.4.4", '', '.')
			.addTrailer(93, "SignatureLength", false, FieldType.INTEGER)
			.addTrailer(89, "Signature", false, FieldType.STRING)
			.addTrailer(10, "CheckSum", true, FieldType.RESERVED)

			.addHeader(8, "BeginString", true, FieldType.RESERVED)
			.addHeader(9, "BodyLen", true, FieldType.RESERVED)
			.addHeader(35, "MsgType", true, FieldType.STRING)
			.addHeader(49, "SenderCompID", true, FieldType.STRING)
			.addHeader(56, "TargetCompID", true, FieldType.STRING)
			
			.addHeader(115, "OnBehalfOfCompID", false, FieldType.STRING)
			.addHeader(128, "DeliverToCompID", false, FieldType.STRING)
			.addHeader(90, "SecureDataLen", false, FieldType.INTEGER)
			.addHeader(91, "SecureData", false, FieldType.STRING)
			
			.addHeader(34, "MsgSeqNum", true, FieldType.INTEGER)
			
			.addHeader(50, "SenderSubID", false, FieldType.STRING)
			.addHeader(142, "SenderLocationID", false, FieldType.STRING)
			.addHeader(57, "TargetSubID", false, FieldType.STRING)
			.addHeader(143, "TargetLocationID", false, FieldType.STRING)
			
			.addHeader(116, "OnBehalfOfSubID", false, FieldType.STRING)
			.addHeader(144, "OnBehalfOfLocationID", false, FieldType.STRING)
			.addHeader(129, "DeliverToSubID", false, FieldType.STRING)
			.addHeader(145, "DeliverToLocationID", false, FieldType.STRING)
			.addHeader(43, "PossDupFlag", false, FieldType.BOOLEAN)
			.addHeader(97, "PossResend", false, FieldType.BOOLEAN)
			
			.addHeader(52, "SendingTime", true, FieldType.UTC_TIMESTAMP)
			
			.addHeader(122, "OrigSendingTime", false, FieldType.UTC_TIMESTAMP)
			.addHeader(212, "XmlDataLen", false, FieldType.UTC_TIMESTAMP)
			.addHeader(213, "XmlData", false, FieldType.STRING)
			.addHeader(347, "MessageEncoding", false, FieldType.STRING)
			.addHeader(369, "LastMsgSeqNumProcessed", false, FieldType.INTEGER)
		//	.addHeader(52, "NoHops", true, FieldType.UTC_TIMESTAMP)
		//	.addHeader(52, "HopCompID", true, FieldType.UTC_TIMESTAMP)
		//	.addHeader(52, "HopSendingTime", true, FieldType.UTC_TIMESTAMP)
		//	.addHeader(52, "HopRefID", true, FieldType.UTC_TIMESTAMP)
		;
		
		// Logon
		dictionary.register("A")
			.addField( 98, "EncryptMethod", true, FieldType.INTEGER, 0)
			.addField(108, "HeartBtInt", true, FieldType.INTEGER)
			.addField( 95, "RawDataLength", false, FieldType.INTEGER)
			.addField( 96, "RawData", false, FieldType.STRING)
			.addField(141, "ResetSeqNumFlag", false, FieldType.INTEGER)
			.addField(789, "NextExpectedMsgSeqNum", false, FieldType.INTEGER)
			.addField(383, "MaxMessageSize", false, FieldType.INTEGER)
			.addGroup(384, "NoMsgTypes", false, new FixMessageFieldTemplate[] {
					new FixMessageFieldTemplate(372, "RefMsgType", false, FieldType.STRING),
					new FixMessageFieldTemplate(385, "MsgDirection", false, FieldType.CHAR)
				})
			.addField(464, "TestMessageIndicator", false, FieldType.BOOLEAN)
			.addField(553, "Username", false, FieldType.STRING)
			.addField(554, "Password", false, FieldType.STRING)
			.addField(25035, "MessageHandling", true, FieldType.INTEGER)
		;
		// Logout
		dictionary.register("5")
			.addField(  58, "Text", false, FieldType.STRING)
			.addField( 354, "EncodedTextLen", false, FieldType.INTEGER)
			.addField( 355, "EncodedText", false, FieldType.STRING)
		;
		// HB
		dictionary.register("0")
			.addField( 112, "TestReqID", false, FieldType.STRING)
		;
		// TestRequest
		dictionary.register("1")
			.addField( 112, "TestReqID", true, FieldType.STRING)
		;		
		// ResendRequest
		dictionary.register("2")
			.addField( 7, "BeginSeqNo", true, FieldType.INTEGER)
			.addField( 16, "EndSeqNo", true, FieldType.INTEGER)
		;	
		// SequenceReset
		dictionary.register("4")
			.addField( 123, "GapFillFlag", false, FieldType.BOOLEAN)
			.addField( 36, "NewSeqNo", true, FieldType.INTEGER)
		;		
		// Reject
		dictionary.register("3")
			.addField(  45, "RefSeqNum", true, FieldType.INTEGER)
			.addField( 371, "RefTagID", false, FieldType.INTEGER)
			.addField( 372, "RefMsgType", false, FieldType.STRING)
			.addField( 373, "SessionRejectReason", false, FieldType.INTEGER)
			.addField(  58, "Text", false, FieldType.STRING)
			.addField( 354, "EncodedTextLen", false, FieldType.STRING)
			.addField( 355, "EncodedText", false, FieldType.STRING)	
		;
		// BusinessMessageReject
		dictionary.register("j")
			.addField(  45, "RefSeqNum", false, FieldType.INTEGER)
			.addField( 372, "RefMsgType", true, FieldType.STRING)
			.addField( 379, "BusinessRejectRefID", false, FieldType.STRING)
			.addField( 380, "BusinessRejectReason", true, FieldType.INTEGER)
			.addField(  58, "Text", false, FieldType.STRING)
			.addField( 354, "EncodedTextLen", false, FieldType.STRING)
			.addField( 355, "EncodedText", false, FieldType.STRING)
		;	
		// New Order - Single
		dictionary.register("D")
			.addField(  11, "ClOrdID", true, FieldType.STRING)
			.addField(  21, "HandlInst", true, FieldType.CHAR)
			.addField(  54, "Side", true, FieldType.CHAR)
			.addField(  55, "Symbol", true, FieldType.STRING)
			.addField(  60, "TransactTime", true, FieldType.UTC_TIMESTAMP)
			.addField(  38, "OrderQty", true, FieldType.DECIMAL)
			.addField(  40, "OrdType", true, FieldType.CHAR)
		;	
		
		return dictionary;
	}
	
	
	public static FixMessageDictionary getDictionary() {
		return dictionary;
	}
}
