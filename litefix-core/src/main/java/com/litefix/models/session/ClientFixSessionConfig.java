package com.litefix.models.session;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	private FixMessageDictionary dictionary;
	
	private boolean enableSSL = false;
	private SSLSettings sslSettings;
	
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
	
	public ClientFixSessionConfig enableSSL(SSLSettings sslSettings) {
		this.enableSSL = true;
		this.sslSettings = sslSettings;
		return this;
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

	public ClientFixSessionConfig setSenderCompId(String senderCompId) {
		this.senderCompId = senderCompId;
		this.memSenderCompIdBytes = senderCompId.getBytes();
		return this;
	}

	public String getTargetCompId() {
		return targetCompId;
	}

	public ClientFixSessionConfig setTargetCompId(String targetCompId) {
		this.targetCompId = targetCompId;
		this.memTargetCompIdBytes = targetCompId.getBytes();
		return this;
	}

	public int getHeartBtInt() {
		return heartBtInt;
	}

	public ClientFixSessionConfig setHeartBtInt(int heartBtInt) {
		this.heartBtInt = heartBtInt;
		return this;
	}

	public int getEncryptMethod() {
		return encryptMethod;
	}

	public ClientFixSessionConfig setEncryptMethod(int encryptMethod) {
		this.encryptMethod = encryptMethod;
		return this;
	}

	public char getResetSeqNumFlag() {
		return resetSeqNumFlag;
	}

	public ClientFixSessionConfig setResetSeqNumFlag(char resetSeqNumFlag) {
		this.resetSeqNumFlag = resetSeqNumFlag;
		return this;
	}

	public FixMessageDictionary getDictionary() {
		return dictionary;
	}

	public ClientFixSessionConfig setDictionary(FixMessageDictionary dictionary) {
		this.dictionary = dictionary;
		return this;
	}

	public List<FixServerHost> getServerHosts() {
		return serverHosts;
	}

	public ClientFixSessionConfig setServerHosts(List<FixServerHost> serverHosts) {
		this.serverHosts = serverHosts;
		return this;
	}

	public ClientFixSessionConfig addServer(String host, int port) {
		this.serverHosts.add( new FixServerHost(host, port));
		return this;
	}

	public SSLSettings getSslSettings() {
		return sslSettings;
	}

	public void setSslSettings(SSLSettings sslSettings) {
		this.sslSettings = sslSettings;
	}

	@Override
	public String toString() {
		return "ClientFixSessionConfig [senderCompId=" + senderCompId + ", targetCompId=" + targetCompId
				+ ", memSenderCompIdBytes=" + Arrays.toString(memSenderCompIdBytes) + ", memTargetCompIdBytes="
				+ Arrays.toString(memTargetCompIdBytes) + ", heartBtInt=" + heartBtInt + ", encryptMethod="
				+ encryptMethod + ", resetSeqNumFlag=" + resetSeqNumFlag + ", serverHosts=" + serverHosts
				+ ", dictionary=" + dictionary + ", enableSSL=" + enableSSL + ", sslSettings=" + sslSettings + "]";
	}	
	
}
