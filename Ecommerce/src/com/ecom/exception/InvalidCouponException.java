package com.ecom.exception;

public class InvalidCouponException extends Exception{
	private static final long serivalVersionUID=1L;
	private String message;
	
	public InvalidCouponException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	} 

}
