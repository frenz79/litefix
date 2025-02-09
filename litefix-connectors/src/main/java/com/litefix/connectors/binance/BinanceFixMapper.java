package com.litefix.connectors.binance;

import com.litefix.models.md.enums.OrderType;
import com.litefix.models.md.enums.Side;
import com.litefix.models.md.enums.TimeInForce;

public class BinanceFixMapper {

	public char map(OrderType orderType) {
		switch(orderType) {
		case MARKET: return '1';
		case LIMIT: return '2';
		case STOP: return '3';
		case STOP_LIMIT: return '4';
		default:
			throw new RuntimeException(String.format("Cannot map OrderType:%s", orderType.name()));
		}
	}

	public char map(Side side) {
		switch(side) {
		case BUY: return '1';
		case SELL: return '2';
		default:
			throw new RuntimeException(String.format("Cannot map Side:%s", side.name()));
		}
	}

	public char map(TimeInForce timeInForce) {
		switch(timeInForce) {
		case GOOD_TILL_CANCEL: return '1';
		case IMMEDIATE_OR_CANCEL: return '3';
		case FILL_OR_KILL: return '4';
		default:
			throw new RuntimeException(String.format("Cannot map TimeInForce:%s", timeInForce.name()));
		}
	}

}
