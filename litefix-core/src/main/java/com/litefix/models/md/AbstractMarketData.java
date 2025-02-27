package com.litefix.models.md;

public abstract class AbstractMarketData {

	private final String symbol;
	private long rcvNanoTime;
	
	public AbstractMarketData(String symbol) {
		super();
		this.symbol = symbol;
	}

	public long getRcvNanoTime() {
		return rcvNanoTime;
	}

	public void setRcvNanoTime(long rcvNanoTime) {
		this.rcvNanoTime = rcvNanoTime;
	}

	public String getSymbol() {
		return symbol;
	}
	
}
