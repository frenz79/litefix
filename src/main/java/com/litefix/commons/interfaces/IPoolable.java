package com.litefix.commons.interfaces;

public interface IPoolable {
	
	public static enum Status {
		FREE,
		NOT_FREE
	}
	
	public String getPoolId();
	public Status getStatus();
	public IPoolable setStatus(Status s);
	public IPoolable reset();
}
