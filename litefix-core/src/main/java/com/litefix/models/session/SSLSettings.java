package com.litefix.models.session;

import java.io.InputStream;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLSettings {
	private String trustManagerAlgo = TrustManagerFactory.getDefaultAlgorithm();
	private String keyManagerFactoryAlgo = KeyManagerFactory.getDefaultAlgorithm();
	
	private boolean useInsecureTrustManager = true;
	private String keyStoreType = "PCKS12";
	private String trustStoreType = "PCKS12";
	
	private String keyStorePwd;
	private String trustStorePwd;
	
	private InputStream keyStoreCert;
	private InputStream trustStoreCert;
	
	public String getTrustManagerAlgo() {
		return trustManagerAlgo;
	}

	public SSLSettings setTrustManagerAlgo(String trustManagerAlgo) {
		this.trustManagerAlgo = trustManagerAlgo;
		return this;
	}

	public boolean isUseInsecureTrustManager() {
		return useInsecureTrustManager;
	}

	public SSLSettings setUseInsecureTrustManager(boolean useInsecureTrustManager) {
		this.useInsecureTrustManager = useInsecureTrustManager;
		return this;
	}

	public String getKeyStoreType() {
		return keyStoreType;
	}

	public SSLSettings setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
		return this;
	}

	public String getTrustStoreType() {
		return trustStoreType;
	}

	public SSLSettings setTrustStoreType(String trustStoreType) {
		this.trustStoreType = trustStoreType;
		return this;
	}

	public String getKeyStorePwd() {
		return keyStorePwd;
	}

	public SSLSettings setKeyStorePwd(String keyStorePwd) {
		this.keyStorePwd = keyStorePwd;
		return this;
	}

	public String getTrustStorePwd() {
		return trustStorePwd;
	}

	public SSLSettings setTrustStorePwd(String trustStorePwd) {
		this.trustStorePwd = trustStorePwd;
		return this;
	}

	public InputStream getKeyStoreCert() {
		return keyStoreCert;
	}

	public SSLSettings setKeyStoreCert(InputStream keyStoreCert) {
		this.keyStoreCert = keyStoreCert;
		return this;
	}

	public InputStream getTrustStoreCert() {
		return trustStoreCert;
	}

	public SSLSettings setTrustStoreCert(InputStream trustStoreCert) {
		this.trustStoreCert = trustStoreCert;
		return this;
	}

	public String getKeyManagerFactoryAlgo() {
		return keyManagerFactoryAlgo;
	}

	public void setKeyManagerFactoryAlgo(String keyManagerFactoryAlgo) {
		this.keyManagerFactoryAlgo = keyManagerFactoryAlgo;
	}		
}