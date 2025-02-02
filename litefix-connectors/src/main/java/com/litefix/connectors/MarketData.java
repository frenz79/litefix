package com.litefix.connectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MarketData {

	private final String requestId;
	private final String symbol;
	private String bookId;

	private long rcvNanoTime;
	
	private List<BookLevel> bidLevels = new ArrayList<>();
	private List<BookLevel> askLevels = new ArrayList<>();
	
	public MarketData(String requestId, String symbol, String bookId) {
		super();
		this.requestId = requestId;
		this.symbol = symbol;
		this.bookId = bookId;
	}
	
	public static class BookLevel {
		private BigDecimal price;
		private BigDecimal size;
		
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
			return "[ " + price + " | " + size + " ]";
		}

		public BookLevel setPrice(BigDecimal price) {
			this.price = price;
			return this;
		}

		public BookLevel setSize(BigDecimal size) {
			this.size = size;
			return this;
		}

		public void setPriceAndSize(BigDecimal price, BigDecimal size) {
			this.price = price;
			this.size = size;
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

	public void delBidBestLevel() {
		this.bidLevels.remove(0);
	}

	public void delAskBestLevel() {
		this.askLevels.remove(0);
	}
	
	public void addLevel(BookLevel level, boolean isBid) {
		if (isBid)bidLevels.add(level);
		else askLevels.add(level);
	}
	
	public void addBidLevel(BookLevel level) {
		bidLevels.add(level);
	}
	
	public void addAskLevel(BookLevel level) {
		askLevels.add(level);
	}
	
	public BookLevel getLevel(int id, boolean isBid) {
		return (isBid)
			?this.bidLevels.get(id)
			:this.askLevels.get(id);
	}
	
	public BookLevel getBidLevel(int id) {
		return this.bidLevels.get(id);
	}

	public BookLevel getAskLevel(int id) {
		return this.askLevels.get(id);
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

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public long getRcvNanoTime() {
		return rcvNanoTime;
	}

	public void setRcvNanoTime(long rcvNanoTime) {
		this.rcvNanoTime = rcvNanoTime;
	}
}
