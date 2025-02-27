package com.litefix.commons.exceptions;

public class InvalidSessionException extends Exception {

	public static enum CAUSE {
		FIELD_NOT_SET,
		GENERIC_ERROR
	};
	
	public InvalidSessionException( String msg ) {
		super(msg);
	}
	
	public InvalidSessionException( CAUSE cause, String details ) {
		super(details);
	}
}
