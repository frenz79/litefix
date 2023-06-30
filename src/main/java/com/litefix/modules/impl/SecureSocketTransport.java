package com.litefix.modules.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.litefix.commons.utils.ArrayUtils;
import com.litefix.models.FixMessage;
import com.litefix.modules.ITransport;

// https://github.com/alkarn/sslengine.example/blob/13a9e6cb04713213a8ac3fc7ced06e5ef8cdf720/src/main/java/alkarn/github/io/sslengine/example/NioSslPeer.java
public class SecureSocketTransport extends SocketTransport {

	private SSLEngine engine;

	/**
	 * Will be used to execute tasks that may emerge during handshake in parallel with the server's main thread.
	 */
	protected ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * Will contain this peer's application data in plaintext, that will be later encrypted
	 * using {@link SSLEngine#wrap(ByteBuffer, ByteBuffer)} and sent to the other peer. This buffer can typically
	 * be of any size, as long as it is large enough to contain this peer's outgoing messages.
	 * If this peer tries to send a message bigger than buffer's capacity a {@link BufferOverflowException}
	 * will be thrown.
	 */
	// protected ByteBuffer myAppData;

	/**
	 * Will contain this peer's encrypted data, that will be generated after {@link SSLEngine#wrap(ByteBuffer, ByteBuffer)}
	 * is applied on {@link NioSslPeer#myAppData}. It should be initialized using {@link SSLSession#getPacketBufferSize()},
	 * which returns the size up to which, SSL/TLS packets will be generated from the engine under a session.
	 * All SSLEngine network buffers should be sized at least this large to avoid insufficient space problems when performing wrap and unwrap calls.
	 */
	protected ByteBuffer myNetData;

	/**
	 * Will contain the other peer's (decrypted) application data. It must be large enough to hold the application data
	 * from any peer. Can be initialized with {@link SSLSession#getApplicationBufferSize()} for an estimation
	 * of the other peer's application data and should be enlarged if this size is not enough.
	 */
	protected ByteBuffer peerAppData;

	/**
	 * Will contain the other peer's encrypted data. The SSL/TLS protocols specify that implementations should produce packets containing at most 16 KB of plaintext,
	 * so a buffer sized to this value should normally cause no capacity problems. However, some implementations violate the specification and generate large records up to 32 KB.
	 * If the {@link SSLEngine#unwrap(ByteBuffer, ByteBuffer)} detects large inbound packets, the buffer sizes returned by SSLSession will be updated dynamically, so the this peer
	 * should check for overflow conditions and enlarge the buffer using the session's (updated) buffer size.
	 */
	protected ByteBuffer peerNetData;


	@Override
	public ITransport send(FixMessage message) throws IOException {
	//	myAppData.clear();
	//	myAppData.put(message.getBytes());
	//	myAppData.flip();
		ByteBuffer myAppData = ByteBuffer.wrap(message.getBuffer().array(), 
				message.getHeaderStart(),
				message.getHeaderLen() + message.getBodyLen());
		myAppData.flip();
		
		while (myAppData.hasRemaining()) {
			// The loop has a meaning for (outgoing) messages larger than 16KB.
			// Every wrap call will remove 16KB from the original message and send it to the remote peer.
			myNetData.clear();
			SSLEngineResult result = engine.wrap(myAppData, myNetData);
			switch (result.getStatus()) {
			case OK:
				myNetData.flip();
				while (myNetData.hasRemaining()) {
					socketChannel.write(myNetData);
				}
				log.debug("Message sent to the server: " + message);
				break;
			case BUFFER_OVERFLOW:
				myNetData = enlargePacketBuffer(engine, myNetData);
				break;
			case BUFFER_UNDERFLOW:
				throw new SSLException("Buffer underflow occured after a wrap. I don't think we should ever get here.");
			case CLOSED:
				closeConnection(socketChannel, engine);
				return this;
			default:
				throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
			}
		}
		return this;
	}

	
	/*
	 
	  int size = socketChannel.read( readBuffer );
				
		if ( size>0 ) {
			// Detect delimiters
			byte[] header = beginMessage;
			byte[] incoming = readBuffer.array();
			
			int startDelimiter = ArrayUtils.findArray(incoming, header);
			if ( startDelimiter>=0 ) {
				int pos = startDelimiter + header.length;				
				int end = ArrayUtils.findByte(incoming, (byte)'\001', pos);		
				int packetSize = ArrayUtils.toInteger(incoming, pos, end);				
				int endDelimiter = startDelimiter + header.length + (end-pos) + 1 + packetSize + 7; // Add checksum				
				System.arraycopy(incoming, startDelimiter,	targetBuff.array(), startDelimiter, endDelimiter-startDelimiter);
				targetBuff.limit(endDelimiter);
				// Multipart message
				if ( readBuffer.limit()!=endDelimiter ) {
					readBuffer.limit(endDelimiter);
					readBuffer.compact();
				} else {
					readBuffer.flip();
				}				
				return true;
			} else {
				System.out.println("GARBAGE DETECTED!");
			}
		}
		return false; 		
	  
	 */
	@Override
	public boolean pollMessage(ByteBuffer targetBuff, byte[] beginMessage ) throws IOException {	
		log.debug("About to read from the server...");

        peerNetData.clear();

        int bytesRead = socketChannel.read(peerNetData);
        if (bytesRead > 0) {
           peerNetData.flip();
                
           SSLEngineResult result = engine.unwrap(peerNetData, peerAppData);
                    switch (result.getStatus()) {
                    case OK:
                        peerAppData.flip();
                        log.debug("Server response: " + new String(peerAppData.array()));
                        break;
                    case BUFFER_OVERFLOW:
                        peerAppData = enlargeApplicationBuffer(engine, peerAppData);
                        break;
                    case BUFFER_UNDERFLOW:
                        peerNetData = handleBufferUnderflow(engine, peerNetData);
                        break;
                    case CLOSED:
                        closeConnection(socketChannel, engine);
                        return;
                    default:
                        throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
                    }
         //       }
         //   } else if (bytesRead < 0) {
          //      handleEndOfStream(socketChannel, engine);
          //      return;
          //  }
          //  Thread.sleep(waitToReadMillis);
        }
	}


	// 
	private SSLEngine initSslEngine( String sslProtocol ) throws Exception {
		// create the SSLEngine
		engine = SSLContext.getDefault().createSSLEngine();
		engine.setUseClientMode(true);
		engine.beginHandshake();

		SSLContext context = SSLContext.getInstance(sslProtocol);
		context.init(
				createKeyManagers("./src/main/resources/client.jks", "storepass", "keypass"), 
				createTrustManagers("./src/main/resources/trustedCerts.jks", "storepass"), 
				new SecureRandom());

		SSLSession session = engine.getSession();
	//	myAppData = ByteBuffer.allocate(1024);
		myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
		peerAppData = ByteBuffer.allocate(1024);
		peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

		return engine;
	}

	@Override
	public ITransport connect(String host, int port) throws Exception {
		super.connect(host, port);
		SSLEngine engine = initSslEngine( "TLSv1.2" );		
		engine.beginHandshake();
		doHandshake(socketChannel, engine);    	
		return this;
	}

	protected boolean doHandshake(SocketChannel socketChannel, SSLEngine engine) throws IOException {
		SSLEngineResult result;
		HandshakeStatus handshakeStatus;

		// NioSslPeer's fields myAppData and peerAppData are supposed to be large enough to hold all message data the peer
		// will send and expects to receive from the other peer respectively. Since the messages to be exchanged will usually be less
		// than 16KB long the capacity of these fields should also be smaller. Here we initialize these two local buffers
		// to be used for the handshake, while keeping client's buffers at the same size.
		int appBufferSize = engine.getSession().getApplicationBufferSize();
		ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);
		ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
		myNetData.clear();
		peerNetData.clear();

		handshakeStatus = engine.getHandshakeStatus();
		while (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
			switch (handshakeStatus) {
			case NEED_UNWRAP:
				if (socketChannel.read(peerNetData) < 0) {
					if (engine.isInboundDone() && engine.isOutboundDone()) {
						return false;
					}
					try {
						engine.closeInbound();
					} catch (SSLException e) {
						log.error("This engine was forced to close inbound, without having received the proper SSL/TLS close notification message from the peer, due to end of stream.");
					}
					engine.closeOutbound();
					// After closeOutbound the engine will be set to WRAP state, in order to try to send a close message to the client.
					handshakeStatus = engine.getHandshakeStatus();
					break;
				}
				peerNetData.flip();
				try {
					result = engine.unwrap(peerNetData, peerAppData);
					peerNetData.compact();
					handshakeStatus = result.getHandshakeStatus();
				} catch (SSLException sslException) {
					log.error("A problem was encountered while processing the data that caused the SSLEngine to abort. Will try to properly close connection...");
					engine.closeOutbound();
					handshakeStatus = engine.getHandshakeStatus();
					break;
				}
				switch (result.getStatus()) {
				case OK:
					break;
				case BUFFER_OVERFLOW:
					// Will occur when peerAppData's capacity is smaller than the data derived from peerNetData's unwrap.
					peerAppData = enlargeApplicationBuffer(engine, peerAppData);
					break;
				case BUFFER_UNDERFLOW:
					// Will occur either when no data was read from the peer or when the peerNetData buffer was too small to hold all peer's data.
					peerNetData = handleBufferUnderflow(engine, peerNetData);
					break;
				case CLOSED:
					if (engine.isOutboundDone()) {
						return false;
					} else {
						engine.closeOutbound();
						handshakeStatus = engine.getHandshakeStatus();
						break;
					}
				default:
					throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
				}
				break;
			case NEED_WRAP:
				myNetData.clear();
				try {
					result = engine.wrap(myAppData, myNetData);
					handshakeStatus = result.getHandshakeStatus();
				} catch (SSLException sslException) {
					log.error("A problem was encountered while processing the data that caused the SSLEngine to abort. Will try to properly close connection...");
					engine.closeOutbound();
					handshakeStatus = engine.getHandshakeStatus();
					break;
				}
				switch (result.getStatus()) {
				case OK :
					myNetData.flip();
					while (myNetData.hasRemaining()) {
						socketChannel.write(myNetData);
					}
					break;
				case BUFFER_OVERFLOW:
					// Will occur if there is not enough space in myNetData buffer to write all the data that would be generated by the method wrap.
					// Since myNetData is set to session's packet size we should not get to this point because SSLEngine is supposed
					// to produce messages smaller or equal to that, but a general handling would be the following:
					myNetData = enlargePacketBuffer(engine, myNetData);
					break;
				case BUFFER_UNDERFLOW:
					throw new SSLException("Buffer underflow occured after a wrap. I don't think we should ever get here.");
				case CLOSED:
					try {
						myNetData.flip();
						while (myNetData.hasRemaining()) {
							socketChannel.write(myNetData);
						}
						// At this point the handshake status will probably be NEED_UNWRAP so we make sure that peerNetData is clear to read.
						peerNetData.clear();
					} catch (Exception e) {
						log.error("Failed to send server's CLOSE message due to socket channel's failure.");
						handshakeStatus = engine.getHandshakeStatus();
					}
					break;
				default:
					throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
				}
				break;
			case NEED_TASK:
				Runnable task;
				while ((task = engine.getDelegatedTask()) != null) {
					executor.execute(task);
				}
				handshakeStatus = engine.getHandshakeStatus();
				break;
			case FINISHED:
				break;
			case NOT_HANDSHAKING:
				break;
			default:
				throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
			}
		}

		return true;
	}

	protected ByteBuffer enlargePacketBuffer(SSLEngine engine, ByteBuffer buffer) {
		return enlargeBuffer(buffer, engine.getSession().getPacketBufferSize());
	}

	protected ByteBuffer enlargeApplicationBuffer(SSLEngine engine, ByteBuffer buffer) {
		return enlargeBuffer(buffer, engine.getSession().getApplicationBufferSize());
	}

	/**
	 * Compares <code>sessionProposedCapacity<code> with buffer's capacity. If buffer's capacity is smaller,
	 * returns a buffer with the proposed capacity. If it's equal or larger, returns a buffer
	 * with capacity twice the size of the initial one.
	 *
	 * @param buffer - the buffer to be enlarged.
	 * @param sessionProposedCapacity - the minimum size of the new buffer, proposed by {@link SSLSession}.
	 * @return A new buffer with a larger capacity.
	 */
	protected ByteBuffer enlargeBuffer(ByteBuffer buffer, int sessionProposedCapacity) {
		if (sessionProposedCapacity > buffer.capacity()) {
			buffer = ByteBuffer.allocate(sessionProposedCapacity);
		} else {
			buffer = ByteBuffer.allocate(buffer.capacity() * 2);
		}
		return buffer;
	}

	/**
	 * Handles {@link SSLEngineResult.Status#BUFFER_UNDERFLOW}. Will check if the buffer is already filled, and if there is no space problem
	 * will return the same buffer, so the client tries to read again. If the buffer is already filled will try to enlarge the buffer either to
	 * session's proposed size or to a larger capacity. A buffer underflow can happen only after an unwrap, so the buffer will always be a
	 * peerNetData buffer.
	 *
	 * @param buffer - will always be peerNetData buffer.
	 * @param engine - the engine used for encryption/decryption of the data exchanged between the two peers.
	 * @return The same buffer if there is no space problem or a new buffer with the same data but more space.
	 * @throws Exception
	 */
	protected ByteBuffer handleBufferUnderflow(SSLEngine engine, ByteBuffer buffer) {
		if (engine.getSession().getPacketBufferSize() < buffer.limit()) {
			return buffer;
		} else {
			ByteBuffer replaceBuffer = enlargePacketBuffer(engine, buffer);
			buffer.flip();
			replaceBuffer.put(buffer);
			return replaceBuffer;
		}
	}

	/**
	 * This method should be called when this peer wants to explicitly close the connection
	 * or when a close message has arrived from the other peer, in order to provide an orderly shutdown.
	 * <p/>
	 * It first calls {@link SSLEngine#closeOutbound()} which prepares this peer to send its own close message and
	 * sets {@link SSLEngine} to the <code>NEED_WRAP</code> state. Then, it delegates the exchange of close messages
	 * to the handshake method and finally, it closes socket channel.
	 *
	 * @param socketChannel - the transport link used between the two peers.
	 * @param engine - the engine used for encryption/decryption of the data exchanged between the two peers.
	 * @throws IOException if an I/O error occurs to the socket channel.
	 */
	protected void closeConnection(SocketChannel socketChannel, SSLEngine engine) throws IOException  {
		engine.closeOutbound();
		doHandshake(socketChannel, engine);
		socketChannel.close();
	}

	/**
	 * In addition to orderly shutdowns, an unorderly shutdown may occur, when the transport link (socket channel)
	 * is severed before close messages are exchanged. This may happen by getting an -1 or {@link IOException}
	 * when trying to read from the socket channel, or an {@link IOException} when trying to write to it.
	 * In both cases {@link SSLEngine#closeInbound()} should be called and then try to follow the standard procedure.
	 *
	 * @param socketChannel - the transport link used between the two peers.
	 * @param engine - the engine used for encryption/decryption of the data exchanged between the two peers.
	 * @throws IOException if an I/O error occurs to the socket channel.
	 */
	protected void handleEndOfStream(SocketChannel socketChannel, SSLEngine engine) throws IOException  {
		try {
			engine.closeInbound();
		} catch (Exception e) {
			log.error("This engine was forced to close inbound, without having received the proper SSL/TLS close notification message from the peer, due to end of stream.");
		}
		closeConnection(socketChannel, engine);
	}

	/**
	 * Creates the key managers required to initiate the {@link SSLContext}, using a JKS keystore as an input.
	 *
	 * @param filepath - the path to the JKS keystore.
	 * @param keystorePassword - the keystore's password.
	 * @param keyPassword - the key's passsword.
	 * @return {@link KeyManager} array that will be used to initiate the {@link SSLContext}.
	 * @throws Exception
	 */
	protected KeyManager[] createKeyManagers(String filepath, String keystorePassword, String keyPassword) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		InputStream keyStoreIS = new FileInputStream(filepath);
		try {
			keyStore.load(keyStoreIS, keystorePassword.toCharArray());
		} finally {
			if (keyStoreIS != null) {
				keyStoreIS.close();
			}
		}
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, keyPassword.toCharArray());
		return kmf.getKeyManagers();
	}

	/**
	 * Creates the trust managers required to initiate the {@link SSLContext}, using a JKS keystore as an input.
	 *
	 * @param filepath - the path to the JKS keystore.
	 * @param keystorePassword - the keystore's password.
	 * @return {@link TrustManager} array, that will be used to initiate the {@link SSLContext}.
	 * @throws Exception
	 */
	protected TrustManager[] createTrustManagers(String filepath, String keystorePassword) throws Exception {
		KeyStore trustStore = KeyStore.getInstance("JKS");
		InputStream trustStoreIS = new FileInputStream(filepath);
		try {
			trustStore.load(trustStoreIS, keystorePassword.toCharArray());
		} finally {
			if (trustStoreIS != null) {
				trustStoreIS.close();
			}
		}
		TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(trustStore);
		return trustFactory.getTrustManagers();
	}
}
