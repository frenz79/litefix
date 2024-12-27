package com.litefix.models.session;

import com.litefix.commons.exceptions.BusinessRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException;
import com.litefix.commons.exceptions.SessionRejectMessageException.SESSION_REJECT_REASON;
import com.litefix.models.fixmessage.FixMessageDecoder;
import com.litefix.models.fixmessage.FixMessageEncoder;
import com.litefix.modules.transport.IClientTransport;
import com.litefix.modules.transport.IClientTransportListener;

public class ClientFixSession extends AbstractFixSession implements IClientTransportListener {
	
	public ClientFixSession(IClientTransport transport, ClientFixSessionConfig sessionConfig ) {
		super(transport, sessionConfig);
	}

	public AbstractFixSession doConnect( ) throws Exception {
		((IClientTransport)getTransport( )).connect( sessionConfig.getServerHost(), sessionConfig.getServerPort(), this );
		return this;
	}
	
	public ClientFixSession doLogon( FixMessageEncoder encoder ){	
		if ( encoder==null ) {
			encoder = newEncoder("A");
		}
		buildLogonMessage( encoder );
		sendMessage( encoder );
		return this;
	}
	
	public ClientFixSession doLogon( ){		
		return doLogon( null );
	}
	
	// IClientTransportListener
	@Override
	public void onConnect(boolean status) {
		sessionSM.connected(status);
		getSessionListener().onConnect(status);
	}
				
	private boolean isFixSignature( byte[] buffer, int pos ) {
		return (buffer[pos]=='8' 
				&& buffer[pos+1]=='=' 
				&& buffer[pos+2]=='F' 
				&& buffer[pos+3]=='I' 
				&& buffer[pos+4]=='X' 
		);
	}
	
	@Override
	public void onMessage( byte[] buffer, int from, int len, long rcvNanoTime ) {
		try {
			if ( buffer.length-from<7 || !isFixSignature( buffer, from ) ) {
				// TODO: handle garbage!
				System.out.println("Garbage detected...no fix signature found");
				return;
			}
			
			FixMessageDecoder decoder = newDecoder( buffer, from, len, rcvNanoTime );
			validate( sessionConfig, decoder, decoder.getMsgType() );
			
			// Trace incoming message
			int expectedIncomingSeqNum = traceIncomingMessage( decoder );
			
			if ( !sessionSM.isLoggedOn() ) {
				switch(decoder.getMsgType()) {
				case "A": handleLogonResp(decoder); break;
				case "5": handleLogout(decoder); break;
				default:
					throw new SessionRejectMessageException(
							decoder.getSeqNum(),
							35,
							decoder.getMsgType(),
							SESSION_REJECT_REASON.INVALID_MSGTYPE_35,
							String.format("Only msgType 'A' or '5' supported when not logged in.Discarded msgType:%s",decoder.getMsgType()));
				}
			} else {
				int delta = decoder.getSeqNum() - expectedIncomingSeqNum;
				
				if ( delta>0 ) {
					// ..older message ?
					System.out.println("Discarded old message. Expecting:"+expectedIncomingSeqNum+" but received:"+decoder.getSeqNum() );
				} else if ( delta>0 ) {
					// ..gap detected
					sendMessage( buildGapFillMessage(
							newEncoder( "2" ),
							expectedIncomingSeqNum,
							decoder.getSeqNum()
					));					
				} else {
					switch(decoder.getMsgType()) {
					case "0": handleHeartbeat(decoder); break;
					case "1": handleTestRequest(decoder); break;
					case "2": handleGapFillRequest(decoder); break;
					case "4": handleSequenceReset(decoder); break;
					default:
						IFixMessageListener listener = getMessageListener(decoder.getMsgType());
						if ( listener!=null ) {
							listener.onMessage(decoder);
						} else {
							throw new SessionRejectMessageException(
									decoder.getSeqNum(),
									35,
									decoder.getMsgType(),
									SESSION_REJECT_REASON.INVALID_MSGTYPE_35,
									String.format("Unsupported msgType:%s",decoder.getMsgType()));
						}
						break;
					}
				}
			}
		} catch( BusinessRejectMessageException ex1 ) {
			ex1.printStackTrace();
			
			sendMessage( buildBusinessRejectMessage(
					newEncoder( "j" ),
					ex1));
			
		} catch( SessionRejectMessageException ex2 ) {
			// TODO: Do logout
			ex2.printStackTrace();
			
			sendMessage( buildRejectMessage(
					newEncoder( "3" ),
					ex2));
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}	

	// IClientTransportListener
	@Override
	public void onError(Throwable cause) {
		// TODO Auto-generated method stub
		cause.printStackTrace();
	}
	
	// 35=A
	private void handleLogonResp(FixMessageDecoder decoder) throws SessionRejectMessageException, BusinessRejectMessageException {
		getSessionListener().onLogon(decoder, true);
		sessionSM.logon(true);
	}
	// 35=5
	private void handleLogout(FixMessageDecoder decoder) {
		getSessionListener().onLogout(decoder);
		sessionSM.logon(false);
	}
	// 35=0
	private void handleHeartbeat(FixMessageDecoder decoder) {
		// TODO: code me
	}
	// 35=1
	private void handleTestRequest(FixMessageDecoder decoder) {
		FixMessageEncoder enc = buildHeartbeatMessage( newEncoder( "0" ) )
			.set( 112, decoder.asString(112) ); // TestReqID
		sendMessage( enc );
	}
	
	private void handleSequenceReset(FixMessageDecoder decoder) {
		int NewSeqNo = decoder.asInt(36);
		resetIncomingSequence( NewSeqNo );
	}

	private void handleGapFillRequest(FixMessageDecoder decoder) {
		// TODO: code me
	}
}
