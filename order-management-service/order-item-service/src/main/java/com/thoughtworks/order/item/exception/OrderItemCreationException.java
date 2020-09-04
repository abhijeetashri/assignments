package com.thoughtworks.order.item.exception;

public class OrderItemCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderItemCreationException() {
	}

	public OrderItemCreationException(String message) {
		super(message);
	}
}
