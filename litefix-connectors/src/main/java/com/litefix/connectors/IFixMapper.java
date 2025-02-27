package com.litefix.connectors;

import com.litefix.models.md.enums.ExecType;
import com.litefix.models.md.enums.OrderStatus;
import com.litefix.models.md.enums.OrderType;
import com.litefix.models.md.enums.Side;
import com.litefix.models.md.enums.TimeInForce;

public abstract class IFixMapper {

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

	public OrderType mapOrderType(char c) {
		switch(c) {
		case '1': return OrderType.MARKET;
		case '2': return OrderType.LIMIT;
		case '3': return OrderType.STOP;
		case '4': return OrderType.STOP_LIMIT;
		default:
			throw new RuntimeException(String.format("Cannot map OrderType:%c", c));
		}
	}

	public Side mapSide(char c) {
		switch(c) {
		case '1': return Side.BUY;
		case '2': return Side.SELL;
		default:
			throw new RuntimeException(String.format("Cannot map Side:%c", c));
		}
	}

	public TimeInForce mapTimeInForce(char c) {
		switch(c) {
		case '1' : return TimeInForce.GOOD_TILL_CANCEL;
		case '3' : return TimeInForce.IMMEDIATE_OR_CANCEL;
		case '4' : return TimeInForce.FILL_OR_KILL;
		default:
			throw new RuntimeException(String.format("Cannot map TimeInForce:%c", c));
		}
	}

	public ExecType mapExecType(char c) {
		switch(c) {
		case '0' : return ExecType.NEW;
		case '4' : return ExecType.CANCELED;
		case '5' : return ExecType.REPLACED;
		case '8' : return ExecType.REJECTED;
		case 'F' : return ExecType.TRADE;
		case 'C' : return ExecType.EXPIRED;
		default:
			throw new RuntimeException(String.format("Cannot map ExecType:%c", c));
		}
	}
	
	public OrderStatus mapOrdStatus(char c) {
		switch(c) {
		case '0' : return OrderStatus.NEW;
		case '1' : return OrderStatus.PARTIALLY_FILLED;
		case '2' : return OrderStatus.FILLED;
		case '4' : return OrderStatus.CANCELED;
		case '6' : return OrderStatus.PENDING_CANCEL;
		case '8' : return OrderStatus.REJECTED;
		case 'A' : return OrderStatus.PENDING_NEW;
		case 'C' : return OrderStatus.EXPIRED;
		default:
			throw new RuntimeException(String.format("Cannot map OrderStatus:%c", c));
		}
	}
}