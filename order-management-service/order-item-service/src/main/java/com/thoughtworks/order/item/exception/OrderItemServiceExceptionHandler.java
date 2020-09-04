package com.thoughtworks.order.item.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderItemServiceExceptionHandler {

	@ExceptionHandler(value = OrderItemNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleOrderNotFoundException(OrderItemNotFoundException exception) {
		return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_FOUND.getReasonPhrase(),
				HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = OrderItemCreationException.class)
	public ResponseEntity<ErrorDetails> handleOrderCreationException(OrderItemCreationException exception) {
		return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),
				HttpStatus.NOT_ACCEPTABLE.value(), exception.getMessage(), LocalDateTime.now()),
				HttpStatus.NOT_ACCEPTABLE);
	}
}
