package com.thoughtworks.order.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderServiceExceptionHandler {

	@ExceptionHandler(value = OrderNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleOrderNotFoundException(OrderNotFoundException exception) {
		return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_FOUND.getReasonPhrase(),
				HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = OrderCreationException.class)
	public ResponseEntity<ErrorDetails> handleOrderCreationException(OrderCreationException exception) {
		return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),
				HttpStatus.NOT_ACCEPTABLE.value(), exception.getMessage(), LocalDateTime.now()),
				HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(value = IncorrectOrderItemException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectOrderItemException(IncorrectOrderItemException exception) {
		return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.getReasonPhrase(),
				HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
	}
}
