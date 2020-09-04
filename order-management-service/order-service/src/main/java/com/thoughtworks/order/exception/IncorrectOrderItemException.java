package com.thoughtworks.order.exception;

public class IncorrectOrderItemException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IncorrectOrderItemException() {
	}

	public IncorrectOrderItemException(String message) {
		super(message);
	}
}
