package com.litefix.connectors;

import java.util.ArrayList;
import java.util.List;

public class Book<P extends Comparable<P>,Q extends Comparable<Q>> {

	private final String requestId;
	private final String symbol;
	private String bookId;
	private final int depth;

	private long rcvNanoTime;
	
	private List<BookLevel<P,Q>> bidLevels = new ArrayList<>();
	private List<BookLevel<P,Q>> askLevels = new ArrayList<>();
	
	public Book(String requestId, String symbol, String bookId, int depth) {
		super();
		this.requestId = requestId;
		this.symbol = symbol;
		this.bookId = bookId;
		this.depth = depth;
	}
	
	public static class BookLevel<P extends Comparable<P>,Q extends Comparable<Q>> {
		private P price;
		private Q qty;
		
		public BookLevel(P price, Q qty) {
			super();
			this.price = price;
			this.qty = qty;
		}

		public P getPrice() {
			return price;
		}

		public Q getSize() {
			return qty;
		}

		@Override
		public String toString() {
			return "[ " + price + " | " + qty + " ]";
		}

		public BookLevel<P,Q> setPrice(P price) {
			this.price = price;
			return this;
		}

		public BookLevel<P,Q> setSize(Q qty) {
			this.qty = qty;
			return this;
		}

		public void setPriceAndSize(P price, Q qty) {
			this.price = price;
			this.qty = qty;
		}
	}

	private int findForPrice( P price, List<BookLevel<P,Q>> l) {
		int size = l.size();
		for ( int i=0; i<size; i++ ) {
			if ( price.compareTo(l.get(i).price)==0 ) {
				return i;
			}
		}
		return -1;
	}
	
	public BookLevel<P,Q> findForPrice( P price, boolean isBid) {
		List<BookLevel<P,Q>> l = (isBid)?bidLevels:askLevels;
		int idx = findForPrice(price,l);
		if ( idx<0 ) {
			return null;
		}
		return l.get(idx);
	}
	
	public List<BookLevel<P,Q>> getBidLevels() {
		return bidLevels;
	}

	public void setBidLevels(List<BookLevel<P,Q>> bidLevels) {
		this.bidLevels = bidLevels;
	}

	public List<BookLevel<P,Q>> getAskLevels() {
		return askLevels;
	}

	public void setAskLevels(List<BookLevel<P,Q>> askLevels) {
		this.askLevels = askLevels;
	}

	public boolean delLevel(P price, boolean isBid) {
		List<BookLevel<P,Q>> l = (isBid)?bidLevels:askLevels;
		int idx = findForPrice(price,l);
		if ( idx<0 ) {
			return false;
		}
		l.remove(idx);
		return true;
	}
	
	public boolean delBestLevel(boolean isBid) {
		List<BookLevel<P,Q>> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return  false;
		}
		return l.remove(0)!=null;
	}
	
	public void addLevel(P price, Q qty, boolean isBid) {
		this.addLevel(new BookLevel<P,Q>(price, qty), isBid);
	}
	
	public void addLevel(BookLevel<P,Q> level, boolean isBid) {
		List<BookLevel<P,Q>> l = (isBid)?bidLevels:askLevels;
		int size = l.size();
		
		if ( size==0 ) {
			l.add(level);
			return;
		}
		
		P priceToIns = level.price;
		if (isBid) {
			// desc
			for ( int i=0; i<size; i++ ) {
				if ( priceToIns.compareTo(l.get(i).price)>=0 ) {
					l.add(i, level);
					return;
				}
			}
			l.add(level);
		} else {
			// asc
			for ( int i=0; i<size; i++ ) {
				if ( priceToIns.compareTo(l.get(i).price)<=0 ) {
					l.add(i, level);
					return;
				}
			}
			l.add(level);
		}
	}
	
	public boolean setBestLevel(P price, Q qty, boolean isBid) {
		return setBestLevel( new BookLevel<>(price, qty), isBid );
	}
	
	public boolean setBestLevel(BookLevel<P,Q> level,boolean isBid) {
		List<BookLevel<P,Q>> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return l.add(level);
		}
		l.get(0).setPriceAndSize(level.getPrice(), level.getSize());
		return true;
	}
	
	public BookLevel<P,Q> getBestLevel(boolean isBid) {
		List<BookLevel<P,Q>> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return null;
		}		
		return l.get(0);
	}
	
	public BookLevel<P,Q> getLevel(P price, boolean isBid) {
		return findForPrice(price, isBid);
	}
	
	public BookLevel<P,Q> getBidLevel(int id) {
		return this.bidLevels.get(id);
	}

	public BookLevel<P,Q> getAskLevel(int id) {
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
