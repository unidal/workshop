package com.site.kernel.dal.model;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = -3035232338172164601L;

   public ValidationException(String msg) {
		super(msg);
	}
}