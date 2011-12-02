package com.site.wdbc;

public class WdbcException extends Exception {
	private static final long serialVersionUID = -5959115909118912691L;

	public WdbcException(String message) {
		super(message);
	}

	public WdbcException(String message, Throwable cause) {
		super(message, cause);
	}
}
