package com.litefix.models.md;

import com.litefix.commons.utils.MathUtils;

public class Trade {
	
	private final String requestId;
	private final String symbol;
	private long rcvNanoTime;
	
	private String tradeId;
	private long qty;
	private long price;
	private char side;
	
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

	public void setRcvNanoTime(long rcvNanoTime) {
		this.rcvNanoTime = rcvNanoTime;
	}

	public long getRcvNanoTime() {
		return rcvNanoTime;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public void setPrice(String price) {
		this.price = MathUtils.toUnscaledNumber(price);
	}

	public void setQty(String qty) {
		this.qty = MathUtils.toUnscaledNumber(qty);
	}

	public void setSide(char side) {
		this.side = side;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getTradeId() {
		return tradeId;
	}

	public char getSide() {
		return side;
	}

	@Override
	public String toString() {
		return "Trade [requestId=" + requestId + ", symbol=" + symbol + ", rcvNanoTime=" + rcvNanoTime + ", tradeId="
				+ tradeId + ", qty=" + qty + ", price=" + price + ", side=" + side + "]";
	}
}
