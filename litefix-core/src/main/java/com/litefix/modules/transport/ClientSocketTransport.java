package com.litefix.modules.transport;

import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import com.litefix.models.session.SSLSettings;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

// https://github.com/devsunny/netty-ssl-example/blob/master/src/main/java/com/asksunny/ssl/SecureSokcetTrustManagerFactory.java
public class ClientSocketTransport implements IClientTransport {

	private EventLoopGroup workerGroup;
	private final String fixBeginString;
	private final char fieldSep;
	private ChannelFuture channelFuture;

	private Bootstrap bootstrap;
	
	public ClientSocketTransport( String fixBeginString, char fieldSep ) {
		this.fixBeginString = fixBeginString;
		this.fieldSep = fieldSep;
		this.workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory()/*EpollIoHandler.newFactory()*/);	
	}

	private KeyManagerFactory keyManagerFactory;
	private TrustManagerFactory trustManagerFactory;
	
	private void initializeSSL(String host, int port, SSLSettings sslSettings, SocketChannel ch ) throws Exception {	
		if (keyManagerFactory==null) {
			KeyStore keyStore = KeyStore.getInstance( "PKCS12" );//sslSettings.getKeyStoreType() );
			keyStore.load(sslSettings.getKeyStoreCert(), sslSettings.getKeyStorePwd().toCharArray());
	
			keyManagerFactory = KeyManagerFactory.getInstance( sslSettings.getKeyManagerFactoryAlgo() );
			keyManagerFactory.init(keyStore, sslSettings.getKeyStorePwd().toCharArray());
	
			//	KeyStore trustStore = KeyStore.getInstance( "JKS" );//sslSettings.getTrustStoreType() );
			//	trustStore.load(sslSettings.getTrustStoreCert(), sslSettings.getTrustStorePwd().toCharArray());
	
			trustManagerFactory = null;
			if ( sslSettings.isUseInsecureTrustManager() ) {
				trustManagerFactory = InsecureTrustManagerFactory.INSTANCE; // Not secure, for testing only
			} else {
				//	trustManagerFactory = TrustManagerFactory.getInstance( sslSettings.getTrustManagerAlgo() );
				//	trustManagerFactory.init(trustStore);
			}
		}
		
		SslContext sslContext = SslContextBuilder.forClient()
				//.protocols("TLSv1.2")
				.keyManager(keyManagerFactory)
				.trustManager(trustManagerFactory)
				.sessionTimeout(0) 
				//.option(SslContextOption<T>, null)
				//.ciphers(Collections.singletonList("TLS_AES_128_GCM_SHA256"))
				//.sslProvider(SslProvider.OPENSSL)
				.build();

		ch.pipeline().addFirst( sslContext.newHandler(ch.alloc() , host, port ));
	}

	@Override
	public void connect(String host, int port, SSLSettings sslSettings, final ITransportListener listener ) throws Exception {		
		try {
			this.bootstrap = new Bootstrap()
					.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_RCVBUF, 1024*1024)
					.remoteAddress(host, port)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
					;
			this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();

					if ( sslSettings!=null ) {
						initializeSSL( host, port, sslSettings, ch );
					}					

					//		pipeline.addLast(new ReadTimeoutHandler(5000));
					//		pipeline.addLast(new WriteTimeoutHandler(5000));
					//		pipeline.addLast(new IdleStateHandler(0, 0, 1, TimeUnit.SECONDS));
					
					pipeline.addLast(new FixLengthFieldBasedFrameDecoder( fixBeginString, fieldSep ));
					pipeline.addLast(new ByteArrayDecoder());
					pipeline.addLast(new ByteArrayEncoder());

					pipeline.addLast("SimpleChannelInboundHandler", new SimpleChannelInboundHandler<byte[]>() {

					//	@Override
					//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
					//		System.out.println("> channelActive");
					//		listener.onConnect(true);
					//	}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							//stop();
							System.out.println("> channelInactive");
							listener.onConnect(false);
						}

						@Override 
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { 
							//stop();
							System.out.println("> exceptionCaught");
							listener.onError( cause );
						}

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, byte[] buffer) throws Exception {
							listener.onMessage( buffer, 0, buffer.length, System.nanoTime() );
						}
					});
				}
			});

			// Start the client.
			this.channelFuture = bootstrap.connect(host, port).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					System.out.println("> operationComplete");
					if (future.isDone()) {
						listener.onConnect(future.isSuccess());
					}
				}
			});

		} finally {

		}
	}

	@Override
	public boolean send(byte[] buffer) throws Exception {
		Channel ch = this.channelFuture.channel();

		//if ( this.channel.isActive() && this.channel.isWritable() ) {
		ChannelFuture cf = ch.writeAndFlush(buffer);
		return cf.await().isSuccess();
	}

	@Override
	public void disconnect() throws Exception {
		System.out.println("> stop");
		this.channelFuture.channel().closeFuture().sync();
	}

	@Override
	public void shutdown() throws IOException {
		System.out.println("> stop");      
		this.workerGroup.shutdownGracefully();
	}
}
