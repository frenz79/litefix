# Lite FIX
LiteFix library wants to be a simple fully featured FIX (Financial Information eXchange) protocol messaging engine 100% Java written and with minimal external dependencies.
LiteFix wants to be easy to be used and as fast as possible.

## Usage

### Starting an initiator
Following and example on how to create an initiator, starting the connection and logging-in to a server:

```
		ClientFixSession session =(ClientFixSession)new FixSessionBuilder(serverHost, serverPort, listener )
				.withBeginString(IFixConst.BEGIN_STRING_FIX44)
				.withSenderCompId(senderCompId)
				.withTargetCompId(targetCompId)
				.withHbIntervalSec(30)
				.withLogonTimeoutSec(5)
				.withAutomaticLogonOnConnect(true)
				.withAutomaticLogonOnLogout(true)
				.withAutomaticReconnect(true, 500L)
				.build();				
		session.doConnectAndRetry( 1000L );
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