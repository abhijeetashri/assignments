package com.dk.exception;

public class ParkingLotException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParkingLotException() {
		super();
	}

	public ParkingLotException(String message) {
		super(message);
	}

	public ParkingLotException(String message, Throwable cause) {
		super(message, cause);
	}
}
