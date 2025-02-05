package com.litefix.connectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Book {

	private final String requestId;
	private final String symbol;
	private String bookId;
	private final int depth;

	private long rcvNanoTime;
	
	private List<BookLevel> bidLevels = new ArrayList<>();
	private List<BookLevel> askLevels = new ArrayList<>();
	
	public Book(String requestId, String symbol, String bookId, int depth) {
		super();
		this.requestId = requestId;
		this.symbol = symbol;
		this.bookId = bookId;
		this.depth = depth;
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

	private int findForPrice( BigDecimal price, List<BookLevel> l) {
		int size = l.size();
		for ( int i=0; i<size; i++ ) {
			if ( price.compareTo(l.get(i).price)==0 ) {
				return i;
			}
		}
		return -1;
	}
	
	public BookLevel findForPrice( BigDecimal price, boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		int idx = findForPrice(price,l);
		if ( idx<0 ) {
			return null;
		}
		return l.get(idx);
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

	public boolean delLevel(BigDecimal price, boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		int idx = findForPrice(price,l);
		if ( idx<0 ) {
			return false;
		}
		l.remove(idx);
		return true;
	}
	
	public boolean delBestLevel(boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return  false;
		}
		return l.remove(0)!=null;
	}
	
	public void addLevel(BookLevel level, boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		int size = l.size();
		
		if ( size==0 ) {
			l.add(level);
		}
		
		BigDecimal priceToIns = level.price;
		
		if (isBid) {
			// asc
			for ( int i=0; i<size; i++ ) {
				if ( priceToIns.compareTo(l.get(i).price)>=0 ) {
					l.add(i, level);
				}
			}
		} else {
			// desc
			for ( int i=0; i<size; i++ ) {
				if ( priceToIns.compareTo(l.get(i).price)<=0 ) {
					l.add(i, level);
				}
			}
		}
	}
	
	public boolean setBestLevel(BookLevel level,boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return l.add(level);
		}
		l.get(0).setPriceAndSize(level.getPrice(), level.getSize());
		return true;
	}
	
	public BookLevel getBestLevel(boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return null;
		}		
		return l.get(0);
	}
	
	public void addBidLevel(BookLevel level) {
		bidLevels.add(level);
	}
	
	public void addAskLevel(BookLevel level) {
		askLevels.add(level);
	}
	
	public BookLevel getLevel(BigDecimal price, boolean isBid) {
		return findForPrice(price, isBid);
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

	public int getDepth() {
		return depth;
	}
}
