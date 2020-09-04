package com.dk.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dk.service.ChargesCalculationService;
import com.dk.service.StorageService;

public class ParkingLot {

	private static final Logger logger = Logger.getLogger(ParkingLot.class.getName());

	private final int capacity;
	private Map<Integer, Integer> floorWiseSlots;
	private TreeSet<Integer> availableSlots;
	private ChargesCalculationService calculationService;

	private ParkingLot(int capacity, int floors) {
		this.capacity = capacity;
		this.availableSlots = new TreeSet<>();
		initialize();
	}

	private ParkingLot(int capacity) {
		this.capacity = capacity;
		this.availableSlots = new TreeSet<>();
		initialize();
	}

	private void initialize() {
		floorWiseSlots = new HashMap<>();
		for (int i = 1; i <= capacity; i++) {
			this.availableSlots.add(i);
		}
		this.calculationService = new ChargesCalculationService();
	}

	public void floorWiseCapacity(int floorNumber, int capacityOfFloor) {
		floorWiseSlots.put(floorNumber, capacityOfFloor);
		// 0 - 10
		// 1 - 20
		// 2 - 30
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isFull() {
		return this.availableSlots.isEmpty();
	}

	/**
	 * Synchronized method to avoid dirty reads/leave system in inconsistent state.
	 * 
	 * @param aVehicle
	 * @return
	 */
	public synchronized ParkingTicket parkVehicle(VehicleRegistration aVehicle) {
		if (this.isFull()) {
			// Parking is full.
			// No ticket assigned.
			logger.log(Level.SEVERE, "Sorry parking lot is full");
			return null;
		}

		// Get nearest slot from entry point
		Integer slotNumber = this.availableSlots.first();
		ParkingTicket newParkingTicket = new ParkingTicket(slotNumber);
		aVehicle.setTicket(newParkingTicket);

		// Slot number is unavailable now.
		this.availableSlots.remove(slotNumber);
		StorageService.getInstance().saveVehicleParkingDetails(aVehicle);
		System.out.println("Allocated slot number: " + newParkingTicket.getSlotNumber());
		return newParkingTicket;
	}

	/**
	 * Synchronized method to avoid dirty reads/leave system in inconsistent state.
	 * 
	 */
	public synchronized int leave(String vehicleNumber, String hours) {
		VehicleRegistration aVehicle = StorageService.getInstance().getVehicleWithRegdNumber(vehicleNumber);
		if (aVehicle != null) {
			ParkingTicket aTicket = aVehicle.getTicket();
			this.availableSlots.add(aTicket.getSlotNumber());
			StorageService.getInstance().removeVehicleParkingDetails(aVehicle);
			int charge = this.calculationService.calculateCharges(Integer.parseInt(hours));
			System.out.println("Registration number " + aVehicle.getRegistrationNumber() + " with slot number "
					+ aTicket.getSlotNumber() + " is free with charge " + charge);
			return charge;
		}
		return 0;
	}

	/**
	 * Synchronized method to avoid dirty reads/leave system in inconsistent state.
	 * 
	 */
	public synchronized int exitByTicket(ParkingTicket aTicket) {
		this.availableSlots.add(aTicket.getSlotNumber());
		VehicleRegistration aVehicle = StorageService.getInstance().getVehicleForTicket(aTicket.getTicketId());
		StorageService.getInstance().removeVehicleParkingDetails(aVehicle);
		return this.calculationService.calculateParkingCharges(aTicket);
	}

	/**
	 * Synchronized method to avoid dirty reads/leave system in inconsistent state.
	 * 
	 */
	public synchronized void dashboard() {
		Map<String, VehicleRegistration> allVehicles = StorageService.getInstance().getVehicleRegistration();
		System.out.println("Slot No.	Registration No.");
		for (Entry<String, VehicleRegistration> aVehicle : allVehicles.entrySet()) {
			String regdNumber = aVehicle.getKey();
			Integer slotNumber = aVehicle.getValue().getTicket().getSlotNumber();
			System.out.println(slotNumber + "		" + regdNumber);
		}
	}

	public void setFloorWiseSlots(Map<Integer, Integer> floorWiseSlots) {
		this.floorWiseSlots = floorWiseSlots;
	}

	public void setAvailableSlots(TreeSet<Integer> availableSlots) {
		this.availableSlots = availableSlots;
	}

	public void setCalculationService(ChargesCalculationService calculationService) {
		this.calculationService = calculationService;
	}
}
