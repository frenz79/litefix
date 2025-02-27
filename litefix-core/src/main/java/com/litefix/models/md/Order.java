package com.litefix.models.md;

import com.litefix.models.md.enums.OrderType;
import com.litefix.models.md.enums.Side;
import com.litefix.models.md.enums.TimeInForce;

public class Order extends AbstractMarketData {
	
	private String orderId;
	private OrderType orderType;
	private long price;
	private Side side;
	private TimeInForce timeInForce;
	private long qty;
	private long visibleQty;
	
	public Order(String symbol) {
		super(symbol);
	}
	
	public String getOrderId() {
		return orderId;
	}
	public Order setOrderId(String orderId) {
		this.orderId = orderId;
		return this;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public Order setOrderType(OrderType orderType) {
		this.orderType = orderType;
		return this;
	}
	public long getPrice() {
		return price;
	}
	public Order setPrice(long price) {
		this.price = price;
		return this;
	}
	public Side getSide() {
		return side;
	}
	public Order setSide(Side side) {
		this.side = side;
		return this;
	}
	public TimeInForce getTimeInForce() {
		return timeInForce;
	}
	public Order setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
		return this;
	}
	public long getQty() {
		return qty;
	}
	public Order setQty(long qty) {
		this.qty = qty;
		return this;
	}
	public long getVisibleQty() {
		return visibleQty;
	}
	public Order setVisibleQty(long visibleQty) {
		this.visibleQty = visibleQty;
		return this;
	}
}
