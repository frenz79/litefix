package com.litefix.models.session;

public class ProxySettings {

	public enum Type {
		SOCKS4,
		SOCKS5,
		HTTP
	}
	
	private Type type;
	private String host;
	private int port;
	private boolean authenticated;
	private String login;
	private String password;
	
	public ProxySettings(Type type, String host, int port) {
		this.type = type;
		this.host = host;
		this.port = port;
		this.authenticated = false;
		this.login = null;
		this.password = null;
	}
	
	public ProxySettings(Type type, String host, int port, String login, String password) {
		super();
		this.type = type;
		this.host = host;
		this.port = port;
		this.authenticated = true;
		this.login = login;
		this.password = password;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
