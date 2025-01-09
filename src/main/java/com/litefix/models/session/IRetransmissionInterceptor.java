package com.litefix.models.session;

import com.litefix.models.fixmessage.FixMessageEncoder;

public interface IRetransmissionInterceptor {

	boolean canRetransmit(FixMessageEncoder msg);

}
