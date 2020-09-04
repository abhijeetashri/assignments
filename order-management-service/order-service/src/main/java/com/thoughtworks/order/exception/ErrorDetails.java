package com.thoughtworks.order.exception;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class ErrorDetails {

	private String httpStatusMsg;
	private int httpStatus;
	private String errorMessage;
	private LocalDateTime timestamp;

}
