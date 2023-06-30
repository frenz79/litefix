package com.litefix.modules.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.litefix.commons.utils.ArrayUtils;
import com.litefix.models.FixMessage;
import com.litefix.modules.ITransport;

public class SocketTransport implements ITransport {

	protected SocketChannel socketChannel;

	public SocketTransport() {
	}

	@Override
	public ITransport connect(String host, int port) throws Exception {
		this.socketChannel = SocketChannel.open();
		this.socketChannel.configureBlocking(true);
		this.socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		this.socketChannel.connect( new InetSocketAddress(InetAddress.getByName(host), port) );
		this.socketChannel.finishConnect();
		this.socketChannel.configureBlocking(false);
		return this;
	}

	@Override
	public ITransport send(FixMessage message) throws IOException {
		this.socketChannel.write(
			ByteBuffer.wrap(message.getBuffer().array(), 
			message.getHeaderStart(),
			message.getHeaderLen() + message.getBodyLen())
		);
		// System.out.println("Written:" + written + " of " + (message.getHeaderLen() +
		// message.getBodyLen()));
		return this;
	}

	@Override
	public void stop() throws IOException {
		this.socketChannel.close();
	}

	private final ByteBuffer readBuffer = ByteBuffer.wrap(new byte[1024*128]); //.allocate(1024*128);

	@Override
	public boolean pollMessage(ByteBuffer targetBuff, byte[] beginMessage ) throws IOException {		
		int bytesRead = socketChannel.read( readBuffer );			
		if ( bytesRead>0 ) {
			return readFixMessage( readBuffer, targetBuff, beginMessage );
		}
		return false;
	}
	
	protected boolean readFixMessage( ByteBuffer readBuffer, ByteBuffer targetBuff, byte[] beginMessage ) {
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
		return false;
	}
}
