package com.dk.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dk.model.ParkingLot;
import com.dk.model.ParkingTicket;
import com.dk.model.VehicleRegistration;

public class ParkingLotTest {

	private ParkingLot parkingLot;

	@Before
	public void initialize() {
		parkingLot = new ParkingLot(1);
	}

	@Test
	public void parkVehicle_Successful_Test() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		ParkingTicket ticket = parkingLot.parkVehicle(aVehicle);
		Assert.assertTrue(ticket.getSlotNumber().equals(1));
	}

	@Test
	public void parkVehicle_ParkingFull_Test() {
		VehicleRegistration v1 = new VehicleRegistration("KA-01-HH-1234");
		ParkingTicket t1 = parkingLot.parkVehicle(v1);
		VehicleRegistration v2 = new VehicleRegistration("KA-01-HH-9999");
		ParkingTicket t2 = parkingLot.parkVehicle(v2);

		Assert.assertNotNull("Parking is full", t1);
		Assert.assertNull("Car parked", t2);
	}

	@Test
	public void parkVehicle_SuccessfulExit_Test() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		ParkingTicket ticket = parkingLot.parkVehicle(aVehicle);
		int fare = parkingLot.exitByTicket(ticket);
		Assert.assertTrue(fare == 10);
	}

	@Test
	public void parkVehicle_WrongCharges_Test() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		ParkingTicket ticket = parkingLot.parkVehicle(aVehicle);
		int fare = parkingLot.exitByTicket(ticket);
		Assert.assertFalse(fare == 30);
	}

	@Test
	public void parkVehicle_LeaveVehicle_Test() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		parkingLot.parkVehicle(aVehicle);
		int fare = parkingLot.leave("KA-01-HH-1234", "4");
		Assert.assertTrue(fare == 30);
	}

	@Test
	public void parkVehicle_LeaveVehicle_WrongFare_Test() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		parkingLot.parkVehicle(aVehicle);
		int fare = parkingLot.leave("KA-01-HH-1234", "4");
		Assert.assertFalse(fare == 20);
	}

	@Test
	public void parkVehicle_LeaveVehicle_LessThanTwoHours_Test() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		parkingLot.parkVehicle(aVehicle);
		int fare = parkingLot.leave("KA-01-HH-1234", "1");
		Assert.assertFalse(fare == 20);
	}

	@Test
	public void parkingLot_Dashboard() {
		VehicleRegistration aVehicle = new VehicleRegistration("KA-01-HH-1234");
		parkingLot.parkVehicle(aVehicle);
		parkingLot.dashboard();
	}
}
