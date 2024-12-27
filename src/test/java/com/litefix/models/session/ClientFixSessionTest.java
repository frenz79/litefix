package com.litefix.models.session;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.litefix.models.dictionary.DefaultFix44Dictionary;
import com.litefix.models.fixmessage.FixMessageEncoder;

class ClientFixSessionTest {

	@Test
	void testFillLogonMessage() {
		ClientFixSessionConfig sessionCfg = new ClientFixSessionConfig();
		sessionCfg.senderCompId = "senderCompId";
		sessionCfg.targetCompId = "targetCompId";
		sessionCfg.serverHost = "serverHost";
		sessionCfg.serverPort = 123456;
		sessionCfg.heartBtInt = 666;
		sessionCfg.resetSeqNumFlag = 'N';
		sessionCfg.dictionary = DefaultFix44Dictionary.init();
		
		FixMessageEncoder enc = FixMessageEncoder.newEncoder( sessionCfg.dictionary, "A" );
		
		ClientFixSession session = new ClientFixSession( null, sessionCfg );
		session.buildLogonMessage(enc);
		
		assertEquals("8=FIX.4.49=5635=A49=senderCompId56=targetCompId34=1108=666141=N10=047", enc.buildString());
	}

}
