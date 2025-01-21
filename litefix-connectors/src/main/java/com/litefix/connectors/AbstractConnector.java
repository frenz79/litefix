package com.litefix.connectors;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.IFixMessageListener;
import com.litefix.models.session.IFixSessionListener;
import com.litefix.models.session.IRetransmissionInterceptor;

public abstract class AbstractConnector implements IFixMessageListener, IFixSessionListener, IRetransmissionInterceptor {
	
	@Override
	public void onConnect(boolean upOrDown) {
		System.out.println("onConnect > "+upOrDown);
	}
	

	@Override
	public void onMessageRcv(FixMessageDecoder decoder) throws BusinessRejectMessageException {
		System.out.println("onmessage > "+decoder);
	}

	@Override
	public void onLogout(FixMessageDecoder decoder) {
		System.out.println("onLogout > "+decoder);
	}

	@Override
	public void onLogon(FixMessageDecoder decoder, boolean result)
			throws SessionRejectMessageException, BusinessRejectMessageException {
		System.out.println("onLogon > "+decoder);
	}
	
	@Override
	public boolean canRetransmit(FixMessageEncoder msg) {
		// Always retransmit
		return true;
	}

	public PrivateKey getPrivateKey(File file, String algo) throws Exception {
		return getPrivateKey(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath()))), algo);
	}
	
	public PrivateKey getPrivateKey(String keyContent, String algo) throws Exception {
        String key = keyContent
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algo);
        return keyFactory.generatePrivate(keySpec);
    }
	
	protected void initLogging() {
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration config = context.getConfiguration();
		/*
		ConsoleAppender consoleAppender  = ConsoleAppender.newBuilder()
				.setName("ConsoleAppender")
				.setTarget(ConsoleAppender.Target.SYSTEM_OUT)
				.setConfiguration(config)
				.setLayout(PatternLayout.newBuilder().setPattern("%-5p %d  [%t] %C{2} (%F:%L) - %m%n").build())
				.build();
		consoleAppender.start();
		*/
		AppenderRef ref = AppenderRef.createAppenderRef("ConsoleAppender", null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        
        LoggerConfig loggerConfig = LoggerConfig.newBuilder()
        		.setLevel(Level.TRACE)
        		.setAdditivity(true)
        		.setLoggerName("*")
        		.setConfig(config)
        		.setRefs(refs)
        		.build();
       // loggerConfig.addAppender(consoleAppender, null, null);
        
        config.addLogger("SESSION_MESSAGES", loggerConfig);
        config.addLogger("SESSION", loggerConfig);
        context.updateLoggers();
	}
}
