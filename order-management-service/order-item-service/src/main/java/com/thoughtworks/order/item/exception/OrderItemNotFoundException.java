package com.thoughtworks.order.item.exception;

public class OrderItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public OrderItemNotFoundException() {
	}

	public OrderItemNotFoundException(String message){
		super(message);
	}
}
