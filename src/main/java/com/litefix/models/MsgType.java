package com.litefix.models;

public class MsgType {

	private int start = 0;
	private int end = 0;
	private byte[] buff;
	private String strValue;
	private TAG tag;
	
	public TAG getTag() {
		return tag;
	}

	public static enum TAG {
		HEARTBEAT("0"),
		TEST_REQUEST("1"),
		RESEND_REQUEST("2"),
		GAP_FILL("4"),
		LOGOUT("5"),
		LOGON("A"),
		OTHER("");

		private final String value;
		
		public String getValue() {
			return value;
		}

		TAG(String value) {
			this.value = value;
		}
		
		public boolean equals( String val ) {
			return this.getValue().equals(val);
		}

		static TAG fromValue(String strValue) {
			if ( "0".equals(strValue)){
				return HEARTBEAT;
			}
			if ( "1".equals(strValue)){
				return TEST_REQUEST;
			}
			if ( "2".equals(strValue)){
				return RESEND_REQUEST;
			}
			if ( "4".equals(strValue)){
				return GAP_FILL;
			}
			if ( "5".equals(strValue)){
				return LOGOUT;
			}
			if ( "A".equals(strValue)){
				return LOGON;
			}
			return OTHER;
		}
	}
	
	private MsgType( byte[] buff, int start, int end ) {
		this.buff = buff;
		this.start = start;
		this.end = end;
		this.strValue = new String(buff, start, end-start);
		this.tag = TAG.fromValue( this.strValue );
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
