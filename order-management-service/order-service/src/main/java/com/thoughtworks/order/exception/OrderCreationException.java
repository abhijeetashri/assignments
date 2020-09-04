package com.thoughtworks.order.exception;

public class OrderCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderCreationException() {
	}

	public OrderCreationException(String message) {
		super(message);
	}
}
