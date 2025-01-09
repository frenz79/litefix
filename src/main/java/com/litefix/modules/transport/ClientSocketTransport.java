package com.litefix.modules.transport;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.GenericFutureListener;

// https://github.com/devsunny/netty-ssl-example/blob/master/src/main/java/com/asksunny/ssl/SecureSokcetTrustManagerFactory.java
public class ClientSocketTransport implements IClientTransport {

	private final EventLoopGroup workerGroup;
	private final String fixBeginString;
	private Channel channel;

	public ClientSocketTransport( String fixBeginString ) {
		this.workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory()/*EpollIoHandler.newFactory()*/);
		this.fixBeginString = fixBeginString;
	}

	/*
		public class WebSocketHandler extends SimpleChannelInboundHandler { 
		  @Override 
		  public void channelRead0(ChannelHandlerContext ctx, 
		                           WebSocketFrame msg) 
		                           throws Exception { 
		    if (msg instanceof TextWebSocketFrame) { 
		      String message = ((TextWebSocketFrame) msg).text(); 
		      // Handle WebSocket message 
		      ctx.writeAndFlush(new TextWebSocketFrame("Echo: " + message)); 
		    } 
		  } 
		}
	 */

	@Override
	public boolean connect(String host, int port, final ITransportListener listener ) throws Exception {		
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
					//		SSLEngine engine = SecureSocketSslContextFactory.getClientContext().createSSLEngine();
					//		engine.setUseClientMode(true);
					//    	pipeline.addLast(new DelimiterBasedFrameDecoder(8192, false, true, delimiter));
					//		pipeline.addLast("ssl", new SslHandler(engine));

					//    	pipeline.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 0));
					pipeline.addLast(new FixLengthFieldBasedFrameDecoder(fixBeginString));
					pipeline.addLast(new ByteArrayDecoder());
					pipeline.addLast(new ByteArrayEncoder());

					// pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));

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
			/*
			CountDownLatch latch = new CountDownLatch(1);
			AtomicBoolean result = new AtomicBoolean(true);
			
			ChannelFuture future = this.channel.writeAndFlush(buffer);
			future.addListener(new GenericFutureListener<DefaultChannelPromise>() {

				@Override
				public void operationComplete(DefaultChannelPromise channelFutures) throws Exception {
					if (channelFutures.isDone()) {
                        if (channelFutures.isSuccess()) {
                        	
                        } else {
                            channelFutures.cause().printStackTrace();
                            result.set(false);
                        }
                    } else if (channelFutures.isCancelled()) {
                        channelFutures.cause().printStackTrace();
                        result.set(false);
                    }
                    latch.countDown();
				}
				
			});
			*/
			/*
			future.addListener(new ChannelGroupFutureListener() {
                @Override
                public void operationComplete(ChannelGroupFuture channelFutures) throws Exception {
                    if (channelFutures.isDone()) {
                        if (channelFutures.isSuccess()) {
                        	
                        } else {
                            channelFutures.cause().printStackTrace();
                            result.set(false);
                        }
                    } else if (channelFutures.isCancelled()) {
                        channelFutures.cause().printStackTrace();
                        result.set(false);
                    }
                    latch.countDown();
                }
            });
			*/
			/*
			try {
				latch.await(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			*/
			//return result.get();
		}
		return false;
	}

	@Override
	public void stop() throws IOException {
		this.channel.close();        
		this.workerGroup.shutdownGracefully();
	}
	
}
