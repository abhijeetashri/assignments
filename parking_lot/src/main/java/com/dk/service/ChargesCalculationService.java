package com.dk.service;

import com.dk.model.ParkingTicket;

public class ChargesCalculationService {

	private static final int DEFAULT_CHARGES = 10;
	private static final int ADDITIONAL_CHARGES = 10;

	public int calculateParkingCharges(ParkingTicket ticket) {
		long exitTime = System.currentTimeMillis();
		long entryTime = ticket.getIssuedAt();

		int hoursParked = (int) ((exitTime - entryTime) / (1000 * 3600));

		if (hoursParked <= 2) {
			return DEFAULT_CHARGES;
		}
		return DEFAULT_CHARGES + ((hoursParked - 2) * ADDITIONAL_CHARGES);
	}

	public int calculateCharges(int hoursParked) {
		if (hoursParked <= 2) {
			return DEFAULT_CHARGES;
		}
		return DEFAULT_CHARGES + ((hoursParked - 2) * ADDITIONAL_CHARGES);
	}
}
