# Lite FIX
LiteFix library wants to be a simple fully featured FIX (Financial Information eXchange) protocol messaging engine 100% Java written and with minimal external dependencies.
LiteFix wants to be easy to be used and as fast as possible.

## Usage

### Starting an initiator
Following and example on how to create an initiator, starting the connection and logging-in to a server:

```
		sessionCfg = new ClientFixSessionConfig()
			.setSenderCompId( "TESTSEND1" )
			.setTargetCompId( "TESTTARGET1" )
			.setHeartBtInt( 1 )
			.setResetSeqNumFlag('N')
			.addServer("localhost", 5178)
			.addServer("localhost", 5179)
			.setDictionary( DefaultFix44Dictionary.init() );
		
		transport = new ClientSocketTransport( sessionCfg.getDictionary().getBeginString(), sessionCfg.getDictionary().getFieldSep() );		
		persistence = new InMemoryPersistence<FixMessageEncoder>();

		session = (ClientFixSession) new ClientFixSession( transport, persistence, sessionCfg )
		.withAllMessagesListener( this )
		.withSessionListener( this )
		.withRetransmissionInterceptor( null );
		
		session.doConnect( true, true );
```

### Sending a 35=D message
To send a NewOrderSingle (35=D) message, just:

```
		FixMessage msg = null;
		try {
			msg = session.getMessagePool().get().setMsgType("D")
				.addField( IFixConst.Symbol, "IT0000000000" )
				.addField( IFixConst.ClOrdID, FixUUID.random() )
				.addField( IFixConst.Currency, "EUR" )
				.addField( IFixConst.HandlInst, '1' )
				.addField( IFixConst.OrderQty, "1000" )
				.addField( IFixConst.OrdType, '1' )
				.addField( IFixConst.Side, '1' )
				.addField( IFixConst.TransactTime, TimeUtils.getSendingTime() );
			session.send(msg);
		} finally {
			session.getMessagePool().release(msg);
		}
```
