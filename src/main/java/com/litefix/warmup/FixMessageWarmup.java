package com.litefix.warmup;

import com.litefix.models.FixMessage;
import com.litefix.models.FixTag;

public class FixMessageWarmup {

	public static void warmup( int counter ) {	
		FixMessage msg = new FixMessage("WARMUP");
		for( int i=0; i<counter; i++ ) {
			msg.setMsgType("D")
			.addField( new FixTag(98), "0" )
			.build("beginString", "senderCompId", "targetCompId", "01012023-00:00:00.000", i);
			
			msg.getMsgType();
			msg.getBodyLen();
			msg.getBuffer();
			msg.getHeaderLen();
			msg.getHeaderStart();
			msg.getStringValue( FixTag.TAG_SYMBOL );
			msg.getIntegerValue( FixTag.HEADER_TAG_SEQ_NUM );
			msg.reset();
		}
	}	
}
