package com.litefix.examples.client;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.models.session.IFixMessageListener;
import com.litefix.models.session.IFixSessionListener;
import com.litefix.models.session.IRetransmissionInterceptor;

public abstract class AbstractClient implements IFixMessageListener, IFixSessionListener, IRetransmissionInterceptor {
	
	@Override
	public boolean canRetransmit(FixMessageEncoder msg) {
		// Always retransmit
		return true;
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
