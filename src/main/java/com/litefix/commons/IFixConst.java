package com.litefix.commons;

import com.litefix.models.FixTag;

public interface IFixConst {

	public static String BEGIN_STRING_FIX44 = "FIX.4.4";
	
	public static FixTag TAG_7 = new FixTag(7); // BeginSeqNo
	public static FixTag TAG_16 = new FixTag(16); // EndSeqNo
	public static FixTag TAG_43 = new FixTag(43); // PossDupFlag 
	
	public static FixTag TAG_58 = new FixTag(58);
	public static FixTag TAG_98 = new FixTag(98);
	public static FixTag TAG_108 = new FixTag(108);
	
	public static FixTag TAG_112 = new FixTag(112);
	public static FixTag TAG_372 = new FixTag(372);

	
	public static FixTag BeginSeqNo = TAG_7; 
	public static FixTag EndSeqNo = TAG_16; 
	public static FixTag PossDupFlag = TAG_43;
}
