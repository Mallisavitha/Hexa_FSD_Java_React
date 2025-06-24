package com.springboot.ecom.exception;

public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;

	public AccessDeniedException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	} 

}
