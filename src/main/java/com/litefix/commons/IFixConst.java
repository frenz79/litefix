package com.litefix.commons;

import com.litefix.models.FixTag;

public interface IFixConst {
	
	public static final byte FIELD_SEPARATOR = '\001';
	public static final String BEGIN_STRING_FIX44 = "FIX.4.4";
	
	public static final FixTag SeqNum = new FixTag(34);
	public static final FixTag SendingTime = new FixTag(52);
	public static final FixTag BeginString = new FixTag(8);
	public static final FixTag BodyLen = new FixTag(9);
	public static final FixTag MsgType = new FixTag(35);
	public static final FixTag SenderCompID = new FixTag(49);
	public static final FixTag TargetCompID = new FixTag(56);
	public static final FixTag BODY_TAG_CHECKSUM = new FixTag(10);
	
	public static final FixTag TAG_7 = new FixTag(7); 
	public static final FixTag TAG_16 = new FixTag(16); 
	public static final FixTag TAG_36 = new FixTag(36); 
	public static final FixTag TAG_34 = new FixTag(16);  
	public static final FixTag TAG_43 = new FixTag(43);  
	
	public static final FixTag TAG_58 = new FixTag(58);
	public static final FixTag TAG_98 = new FixTag(98);
	public static final FixTag TAG_108 = new FixTag(108);
	
	public static final FixTag TAG_112 = new FixTag(112);
	public static final FixTag TAG_372 = new FixTag(372);
	
	public static final FixTag TAG_55  = new FixTag(55);
	public static final FixTag TAG_117 = new FixTag(117);
	public static final FixTag TAG_123 = new FixTag(123);
	public static final FixTag TAG_141 = new FixTag(141);
	public static final FixTag TAG_789 = new FixTag(789);
	
	public static final FixTag TAG_11 = new FixTag(11);
	public static final FixTag TAG_15 = new FixTag(15);
	public static final FixTag TAG_21 = new FixTag(21);
	public static final FixTag TAG_38 = new FixTag(38);
	public static final FixTag TAG_40 = new FixTag(40);
	public static final FixTag TAG_45 = new FixTag(45);
	public static final FixTag TAG_54 = new FixTag(54);
	public static final FixTag TAG_60 = new FixTag(60);
	
	public static final FixTag BeginSeqNo = TAG_7; 
	public static final FixTag EndSeqNo = TAG_16; 
	public static final FixTag NewSeqNo = TAG_36;
	public static final FixTag PossDupFlag = TAG_43;
	public static final FixTag MsgSeqNum = TAG_34;	
	public static final FixTag Symbol = TAG_55;
	public static final FixTag QuoteID = TAG_117;
	public static final FixTag ResetSeqNumFlag = TAG_141;
	public static final FixTag HeartBtInt = TAG_108;
	public static final FixTag EncryptMethod = TAG_98;
	public static final FixTag TestReqID = TAG_112;
	public static final FixTag NextExpectedMsgSeqNum = TAG_789;	
	public static final FixTag GapFillFlag = TAG_789;	
	
	public static final FixTag ClOrdID = TAG_11;	
	public static final FixTag Currency = TAG_15;	
	public static final FixTag HandlInst = TAG_21;	
	public static final FixTag OrderQty = TAG_38;	
	public static final FixTag OrdType = TAG_40;	
	public static final FixTag Side = TAG_54;	
	public static final FixTag TransactTime = TAG_60;
	public static final FixTag RefSeqNum = TAG_45;
	
}
