package com.litefix.connectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MarketData {

	private final String requestId;
	private final String symbol;
	private final String bookId;
	
	private List<BookLevel> bidLevels = new ArrayList<>();
	private List<BookLevel> askLevels = new ArrayList<>();
	
	public void addBidLevel(BookLevel level) {
		bidLevels.add(level);
	}
	
	public void addAskLevel(BookLevel level) {
		askLevels.add(level);
	}
	
	public MarketData(String requestId, String symbol, String bookId) {
		super();
		this.requestId = requestId;
		this.symbol = symbol;
		this.bookId = bookId;
	}
	
	public static class BookLevel {
		private final BigDecimal price;
		private final BigDecimal size;
		
		public BookLevel(BigDecimal price, BigDecimal size) {
			super();
			this.price = price;
			this.size = size;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public BigDecimal getSize() {
			return size;
		}

		@Override
		public String toString() {
			return "BookLevel [price=" + price + ", size=" + size + "]";
		}
	}

	public List<BookLevel> getBidLevels() {
		return bidLevels;
	}

	public void setBidLevels(List<BookLevel> bidLevels) {
		this.bidLevels = bidLevels;
	}

	public List<BookLevel> getAskLevels() {
		return askLevels;
	}

	public void setAskLevels(List<BookLevel> askLevels) {
		this.askLevels = askLevels;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getBookId() {
		return bookId;
	}

	@Override
	public String toString() {
		return "MarketData [requestId=" + requestId + ", symbol=" + symbol + ", bookId=" + bookId + ", bidLevels="
				+ bidLevels + ", askLevels=" + askLevels + "]";
	}
}
