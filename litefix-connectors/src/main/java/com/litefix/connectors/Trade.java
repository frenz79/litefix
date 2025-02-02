package com.litefix.connectors;

public class Trade {

	private final String requestId;
	private final String symbol;
	
	public Trade(String requestId, String symbol) {
		super();
		this.requestId = requestId;
		this.symbol = symbol;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getSymbol() {
		return symbol;
	}
}
