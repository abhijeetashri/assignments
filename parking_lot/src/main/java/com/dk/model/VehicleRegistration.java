package com.dk.model;

public class VehicleRegistration {

	private String registrationNumber;
	private String vehicleColor;
	private ParkingTicket ticket;

	public VehicleRegistration(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

	public String getVehicleColor() {
		return vehicleColor;
	}

	public ParkingTicket getTicket() {
		return ticket;
	}

	public void setTicket(ParkingTicket ticket) {
		this.ticket = ticket;
	}
}
