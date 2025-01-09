package com.litefix.models.session;

import java.util.ArrayList;
import java.util.List;

import com.litefix.models.fixmessage.FixMessageDictionary;

public class ClientFixSessionConfig {

	private String senderCompId;
	private String targetCompId;
	
	private byte[] memSenderCompIdBytes;
	private byte[] memTargetCompIdBytes;
	
	private int heartBtInt = 0;
	private int encryptMethod = 0;
	private char resetSeqNumFlag = 'Y';
	
	private List<FixServerHost> serverHosts = new ArrayList<>();
	
	public FixMessageDictionary dictionary;
	
	public static class FixServerHost {
		private final String name;
		private final int port;
		
		public FixServerHost(String name, int port) {
			super();
			this.name = name;
			this.port = port;
		}

		public String getName() {
			return name;
		}

		public int getPort() {
			return port;
		}

		@Override
		public String toString() {
			return "FixServerHost [name=" + name + ", port=" + port + "]";
		}
	}
	
	public byte[] getSenderCompIdBytes() {
		return memSenderCompIdBytes;
	}

	public byte[] getTargetCompIdBytes() {
		return memTargetCompIdBytes;
	}
	
	public String getSenderCompId() {
		return senderCompId;
	}

	public void setSenderCompId(String senderCompId) {
		this.senderCompId = senderCompId;
		this.memSenderCompIdBytes = senderCompId.getBytes();
	}

	public String getTargetCompId() {
		return targetCompId;
	}

	public void setTargetCompId(String targetCompId) {
		this.targetCompId = targetCompId;
		this.memTargetCompIdBytes = targetCompId.getBytes();
	}

	public int getHeartBtInt() {
		return heartBtInt;
	}

	public void setHeartBtInt(int heartBtInt) {
		this.heartBtInt = heartBtInt;
	}

	public int getEncryptMethod() {
		return encryptMethod;
	}

	public void setEncryptMethod(int encryptMethod) {
		this.encryptMethod = encryptMethod;
	}

	public char getResetSeqNumFlag() {
		return resetSeqNumFlag;
	}

	public void setResetSeqNumFlag(char resetSeqNumFlag) {
		this.resetSeqNumFlag = resetSeqNumFlag;
	}

	public FixMessageDictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(FixMessageDictionary dictionary) {
		this.dictionary = dictionary;
	}

	public List<FixServerHost> getServerHosts() {
		return serverHosts;
	}

	public void setServerHosts(List<FixServerHost> serverHosts) {
		this.serverHosts = serverHosts;
	}

	public void addServer(String host, int port) {
		this.serverHosts.add( new FixServerHost(host, port));
	}	
	
}
