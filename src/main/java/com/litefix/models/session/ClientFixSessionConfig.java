package com.litefix.models.session;

import com.litefix.models.fixmessage.FixMessageDictionary;

public class ClientFixSessionConfig {

	private String senderCompId;
	private String targetCompId;
	
	private byte[] memSenderCompIdBytes;
	private byte[] memTargetCompIdBytes;
	
	private String serverHost;
	private int serverPort;
	private int heartBtInt = 0;
	private int encryptMethod = 0;
	private char resetSeqNumFlag = 'Y';
	
	public FixMessageDictionary dictionary;
	
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

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
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
	
	
}
