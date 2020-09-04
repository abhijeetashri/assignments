package com.dk.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dk.model.ParkingTicket;
import com.dk.model.VehicleRegistration;

public final class StorageService {

	private static final StorageService instance = new StorageService();

	// Maps to store
	private Map<Long, ParkingTicket> parkingTickets = new ConcurrentHashMap<>();

	private Map<String, VehicleRegistration> vehicleRegistration = new ConcurrentHashMap<>();

	private Map<ParkingTicket, VehicleRegistration> ticketVehicle = new ConcurrentHashMap<>();

	private Map<Integer, VehicleRegistration> slotVehicle = new ConcurrentHashMap<>();

	private StorageService() {
		// initialize ticket number with a default value.
		ticketNumber[0] = 1000;
	}

	private long[] ticketNumber = new long[1];

	public static StorageService getInstance() {
		return instance;
	}

	public Map<String, VehicleRegistration> getVehicleRegistration() {
		return new HashMap<>(vehicleRegistration);
	}

	public VehicleRegistration getVehicleWithRegdNumber(String regdNumber) {
		return this.vehicleRegistration.get(regdNumber);
	}

	public VehicleRegistration getVehicleForTicket(Long ticketId) {
		ParkingTicket parkingTicket = this.parkingTickets.get(ticketId);
		return this.ticketVehicle.get(parkingTicket);
	}

	public VehicleRegistration getVehicleAtSlotNumber(Integer slotNumber) {
		return this.slotVehicle.get(slotNumber);
	}

	public synchronized long getNextTicketNumber() {
		long newTicketNumber = ticketNumber[0];
		ticketNumber[0] += 1;
		return newTicketNumber;
	}

	public boolean saveVehicleParkingDetails(VehicleRegistration aVehicle) {
		ParkingTicket parkingTicket = aVehicle.getTicket();
		this.vehicleRegistration.put(aVehicle.getRegistrationNumber(), aVehicle);
		this.ticketVehicle.put(parkingTicket, aVehicle);
		this.slotVehicle.put(parkingTicket.getSlotNumber(), aVehicle);
		this.parkingTickets.put(parkingTicket.getTicketId(), aVehicle.getTicket());
		return true;
	}

	public boolean removeVehicleParkingDetails(VehicleRegistration aVehicle) {
		ParkingTicket parkingTicket = aVehicle.getTicket();
		this.slotVehicle.remove(parkingTicket.getSlotNumber());
		this.parkingTickets.remove(parkingTicket.getTicketId());
		this.ticketVehicle.remove(parkingTicket);
		this.vehicleRegistration.remove(aVehicle.getRegistrationNumber());
		return true;
	}
}
