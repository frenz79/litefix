package com.litefix.models.md;

import java.util.ArrayList;
import java.util.List;

import com.litefix.commons.utils.MathUtils;

public class Book extends AbstractMarketData {

	private final String requestId;
	private String bookId;
	private final int depth;
	
	private List<BookLevel> bidLevels = new ArrayList<>();
	private List<BookLevel> askLevels = new ArrayList<>();
	
	public Book(String requestId, String symbol, String bookId, int depth) {
		super( symbol );
		this.requestId = requestId;
		this.bookId = bookId;
		this.depth = depth;
	}
	
	public static class BookLevel {
		private long price;
		private long qty;
		
		public BookLevel(String price, String qty) {
			this.price = MathUtils.toUnscaledNumber(price);
			this.qty = MathUtils.toUnscaledNumber(qty);
		}
		
		public BookLevel(long price, long qty) {
			this.price = price;
			this.qty = qty;
		}

		public long getPrice() {
			return price;
		}

		public long getSize() {
			return qty;
		}

		@Override
		public String toString() {
			return "[ " + price + " | " + qty + " ]";
		}

		public BookLevel setPrice(long price) {
			this.price = price;
			return this;
		}

		public BookLevel setQty(long qty) {
			this.qty = qty;
			return this;
		}

		public void setPriceAndQty(long price, long qty) {
			setPrice(price);
			setQty(qty);
		}
		
		public BookLevel setPrice(String price) {
			this.price = MathUtils.toUnscaledNumber(price);
			return this;
		}

		public BookLevel setQty(String qty) {
			this.qty = MathUtils.toUnscaledNumber(qty);
			return this;
		}

		public void setPriceAndQty(String price, String qty) {
			setPrice(price);
			setQty(qty);
		}
		
	}

	private int findForPrice( String _price, List<BookLevel> l) {
		long price = MathUtils.toUnscaledNumber(_price);
		int size = l.size();
		for ( int i=0; i<size; i++ ) {
			if ( price == l.get(i).price) {
				return i;
			}
		}
		return -1;
	}
	
	public BookLevel findForPrice( String _price, boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		int idx = findForPrice(_price,l);
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

	public boolean delLevel(String price, boolean isBid) {
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
	
	public void addLevel(String price, String qty, boolean isBid) {
		this.addLevel(new BookLevel(price, qty), isBid);
	}
	
	public void addLevel(long price, long qty, boolean isBid) {
		this.addLevel(new BookLevel(price, qty), isBid);
	}
	
	public void addLevel(BookLevel level, boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		int size = l.size();
		
		if ( size==0 ) {
			l.add(level);
			return;
		}
		
		long priceToIns = level.price;
		if (isBid) {
			// desc
			for ( int i=0; i<size; i++ ) {
				if ( priceToIns>=l.get(i).price ) {
					l.add(i, level);
					return;
				}
			}
			l.add(level);
		} else {
			// asc
			for ( int i=0; i<size; i++ ) {
				if ( priceToIns<=l.get(i).price ) {
					l.add(i, level);
					return;
				}
			}
			l.add(level);
		}
	}
	
	public boolean setBestLevel(String price, String qty, boolean isBid) {
		return setBestLevel( new BookLevel(price, qty), isBid );
	}
	
	public boolean setBestLevel(BookLevel level,boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return l.add(level);
		}
		l.get(0).setPriceAndQty(level.getPrice(), level.getSize());
		return true;
	}
	
	public BookLevel getBestLevel(boolean isBid) {
		List<BookLevel> l = (isBid)?bidLevels:askLevels;
		if ( l.isEmpty() ) {
			return null;
		}		
		return l.get(0);
	}
	
	public BookLevel getLevel(String price, boolean isBid) {
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

	public String getBookId() {
		return bookId;
	}

	@Override
	public String toString() {
		return "MarketData [requestId=" + requestId + ", symbol=" + getSymbol() + ", bookId=" + bookId + ", bidLevels="
				+ bidLevels + ", askLevels=" + askLevels + "]";
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public int getDepth() {
		return depth;
	}
}
