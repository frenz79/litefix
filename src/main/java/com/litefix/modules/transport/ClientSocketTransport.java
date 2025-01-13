package com.litefix.modules.transport;

import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import com.litefix.models.session.SSLSettings;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

// https://github.com/devsunny/netty-ssl-example/blob/master/src/main/java/com/asksunny/ssl/SecureSokcetTrustManagerFactory.java
public class ClientSocketTransport implements IClientTransport {

	private final EventLoopGroup workerGroup;
	private final String fixBeginString;
	private final char fieldSep;
	private Channel channel;
	
	public ClientSocketTransport( String fixBeginString, char fieldSep ) {
		this.workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory()/*EpollIoHandler.newFactory()*/);
		this.fixBeginString = fixBeginString;
		this.fieldSep = fieldSep;
	}
	
	private void initializeSSL(String host, int port, SSLSettings sslSettings, SocketChannel ch ) throws Exception {	
		KeyStore keyStore = KeyStore.getInstance( sslSettings.getKeyStoreType() );
		keyStore.load(sslSettings.getKeyStoreCert(), sslSettings.getKeyStorePwd().toCharArray());
		
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance( sslSettings.getKeyManagerFactoryAlgo() );
		keyManagerFactory.init(keyStore, sslSettings.getKeyStorePwd().toCharArray());

		KeyStore trustStore = KeyStore.getInstance( sslSettings.getTrustStoreType() );
		trustStore.load(sslSettings.getTrustStoreCert(), sslSettings.getTrustStorePwd().toCharArray());
		
		TrustManagerFactory trustManagerFactory = null;
		if ( sslSettings.isUseInsecureTrustManager() ) {
			trustManagerFactory = InsecureTrustManagerFactory.INSTANCE; // Not secure, for testing only
		} else {
			trustManagerFactory = TrustManagerFactory.getInstance( sslSettings.getTrustManagerAlgo() );
			trustManagerFactory.init(trustStore);
		}
		
		SslContext sslContext = SslContextBuilder.forClient()
			    .keyManager(keyManagerFactory)
			    .trustManager(trustManagerFactory)
			    .build();
		
		ch.pipeline().addFirst("ssl", sslContext.newHandler(ch.alloc(), host, port));
	}
	
	@Override
	public boolean connect(String host, int port, SSLSettings sslSettings, final ITransportListener listener ) throws Exception {		
		try {
			Bootstrap b = new Bootstrap()
					.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_RCVBUF, 1024*1024)
					;
			
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					
					if ( sslSettings!=null ) {
						initializeSSL( host, port, sslSettings, ch );
					}					
					
					pipeline.addLast(new FixLengthFieldBasedFrameDecoder( fixBeginString, fieldSep ));
					pipeline.addLast(new ByteArrayDecoder());
					pipeline.addLast(new ByteArrayEncoder());

					pipeline.addLast("myHandler", new SimpleChannelInboundHandler<byte[]>() {

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							listener.onConnect(true);
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							listener.onConnect(false);
						}

						@Override 
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { 
							listener.onError( cause );
							ctx.close(); 
						}

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, byte[] buffer) throws Exception {
							listener.onMessage( buffer, 0, buffer.length, System.nanoTime() );
						}
					});
				}
			});

			// Start the client.
			channel = b.connect(host, port).sync().channel(); // (5)
			return channel.isActive();
		} finally {

		}
	}

	@Override
	public boolean send(byte[] buffer) throws IOException {
		if ( this.channel.isActive() && this.channel.isWritable() ) {
			return this.channel.writeAndFlush(buffer).isSuccess();
		}
		return false;
	}

	@Override
	public void stop() throws IOException {
		this.channel.close();        
		this.workerGroup.shutdownGracefully();
	}
	
}
