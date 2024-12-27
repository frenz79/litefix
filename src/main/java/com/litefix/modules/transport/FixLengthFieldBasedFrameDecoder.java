package com.litefix.modules.transport;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

//Based on LengthFieldBasedFrameDecoder
public class FixLengthFieldBasedFrameDecoder extends ByteToMessageDecoder {
	
	private static final char FIELD_SEPARATOR = '';
	private static final int CRC_BODY_FIELD_SIZE = 8;
	private static final byte BYTE_0 = (byte)'0';
	private static final int POW10_0 = (int)Math.pow(10,0);
	private static final int POW10_1 = (int)Math.pow(10,1);
	private static final int POW10_2 = (int)Math.pow(10,2);
	private static final int POW10_3 = (int)Math.pow(10,3);
	private static final int POW10_4 = (int)Math.pow(10,4);
	
	private final int lengthFieldOffset;
	
	private int frameLengthInt = -1;

	public FixLengthFieldBasedFrameDecoder( String beginString ){
		this.lengthFieldOffset = ("8="+beginString+FIELD_SEPARATOR+"9=").getBytes().length;
	}

	private static final int pow10( int exp ) {
		switch(exp) {
		case 0 : return POW10_0;
		case 1 : return POW10_1;
		case 2 : return POW10_2;
		case 3 : return POW10_3;
		case 4 : return POW10_4;
		default: return (int)Math.pow(10, exp);
		}
	}

	private int getBodyLen(ByteBuf in) {
		int bodyLen = 0;
		byte b[] = new byte[8];
		int i=0;
		int readerOffset = in.readerIndex(); 
		while ( (b[i] = in.getByte(readerOffset+lengthFieldOffset+i))!=FIELD_SEPARATOR) {
			i++;
		};
		for (int j=i-1; j>=0; j--) {
			bodyLen += (b[j]-BYTE_0) * pow10(i-(j+1));
		}
		return bodyLen + i + CRC_BODY_FIELD_SIZE;
	}

	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		int frameLength = 0;
		if (frameLengthInt == -1) { // new frame

			if (in.readableBytes() < in.readerIndex()+lengthFieldOffset) {
				return null;
			}
			frameLength = getBodyLen(in) + lengthFieldOffset;
			// never overflows because it's less than maxFrameLength
			frameLengthInt = frameLength;
		}
		if (in.readableBytes() < frameLengthInt) { // frameLengthInt exist , just check buf
			return null;
		}

		// extract frame
		int readerIndex = in.readerIndex();
		int actualFrameLength = frameLengthInt;
		ByteBuf frame = extractFrame(ctx, in, readerIndex, actualFrameLength);
		in.readerIndex(readerIndex + actualFrameLength);
		frameLengthInt = -1; // start processing the next frame
		return frame;
	}

	@Override
	protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Object decoded = decode(ctx, in);
		if (decoded != null) {
			out.add(decoded);
		}
	}

	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
		return buffer.retainedSlice(index, length);
	}
}