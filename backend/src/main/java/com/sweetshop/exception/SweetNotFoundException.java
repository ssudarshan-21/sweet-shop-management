package com.sweetshop.exception;

public class SweetNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SweetNotFoundException(String message) {
        super(message);
    }
}