package com.litefix.models;

public class MsgType {

	private int start = 0;
	private int end = 0;
	private byte[] buff;
	private String strValue;
	
	private MsgType( byte[] buff, int start, int end ) {
		this.buff = buff;
		this.start = start;
		this.end = end;
		this.strValue = new String(buff, start, end-start);
	}
	
	public static MsgType buildFrom(byte[] buff, int start, int end) {
		return new MsgType(buff, start, end);
	}

	public byte[] getBytes() {
		return buff;
	}

	@Override
	public String toString() {
		return this.strValue;
	}

	public boolean is(String s) {
		if ( s.length()==1 ) {
			return buff[start]==(s.charAt(0) & 0xFF);
		}
		for ( int i=0; i<s.length(); i++) {
			if ( buff[start+i]!=(s.charAt(i) & 0xFF) ) {
				return false;
			}
		}
		return true;
	}

	public boolean in(String ... vals) {
		for ( String val : vals ) {
			if ( is(val) ) {
				return true;
			}
		}
		return false;
	}

}
