package com.litefix.models.md;

import java.time.LocalDateTime;

import com.litefix.models.md.enums.ExecType;
import com.litefix.models.md.enums.OrderStatus;
import com.litefix.models.md.enums.OrderType;
import com.litefix.models.md.enums.Side;
import com.litefix.models.md.enums.TimeInForce;

public class ExecutionReport extends AbstractMarketData {

	private String executionId;
	private String tradeId;
	private String orderId;
	private String marketOrderId;
	private long qty;
	private OrderType orderType;
	private Side side;
	private long price;
	private TimeInForce timeInForce;
	private LocalDateTime transactTime;
	private long visibleQty;
	private ExecType execType;
	private long cumQty;
	private long lastPx;
	private long lastQty;
	private OrderStatus orderStatus;
	private String text;
	
	public ExecutionReport(String symbol) {
		super(symbol);
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMarketOrderId() {
		return marketOrderId;
	}

	public void setMarketOrderId(String marketOrderId) {
		this.marketOrderId = marketOrderId;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public TimeInForce getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
	}

	public LocalDateTime getTransactTime() {
		return transactTime;
	}

	public void setTransactTime(LocalDateTime transactTime) {
		this.transactTime = transactTime;
	}

	public long getVisibleQty() {
		return visibleQty;
	}

	public void setVisibleQty(long visibleQty) {
		this.visibleQty = visibleQty;
	}

	public ExecType getExecType() {
		return execType;
	}

	public void setExecType(ExecType execType) {
		this.execType = execType;
	}

	public long getCumQty() {
		return cumQty;
	}

	public void setCumQty(long cumQty) {
		this.cumQty = cumQty;
	}

	public long getLastPx() {
		return lastPx;
	}

	public void setLastPx(long lastPx) {
		this.lastPx = lastPx;
	}

	public long getLastQty() {
		return lastQty;
	}

	public void setLastQty(long lastQty) {
		this.lastQty = lastQty;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "ExecutionReport [executionId=" + executionId + ", tradeId=" + tradeId + ", orderId=" + orderId
				+ ", marketOrderId=" + marketOrderId + ", qty=" + qty + ", orderType=" + orderType + ", side=" + side
				+ ", price=" + price + ", timeInForce=" + timeInForce + ", transactTime=" + transactTime
				+ ", visibleQty=" + visibleQty + ", execType=" + execType + ", cumQty=" + cumQty + ", lastPx=" + lastPx
				+ ", lastQty=" + lastQty + ", orderStatus=" + orderStatus + ", text=" + text + "]";
	}

	public void setQty(String asString) {
		// TODO Auto-generated method stub
		
	}

	public void setVisibleQty(String asString) {
		// TODO Auto-generated method stub
		
	}

	public void setCumQty(String asString) {
		// TODO Auto-generated method stub
		
	}

	public void setLastQty(String asString) {
		// TODO Auto-generated method stub
		
	}

}
