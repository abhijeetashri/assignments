package com.dk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dk.model.ParkingLot;
import com.dk.model.VehicleRegistration;

public class ParkingLotApp {

	private static final Logger logger = Logger.getLogger(ParkingLotApp.class.getName());

	public static void main(String[] args) {
		logger.info("========= DK Parking Lot =========");
		if(args.length != 1) {
			logger.log(Level.SEVERE, "input file mandatory");
		}
		try {
			List<String> lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
			processParkingLotSystem(lines);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Please check file path validity", e);
		}
	}

	private static void processParkingLotSystem(List<String> commands) {
		try {
			String createParkingLot = commands.get(0);
			if (createParkingLot.startsWith("create_parking_lot")) {
				int capacity = Integer.parseInt(createParkingLot.split(" ")[1]);
				ParkingLot parkingLot = new ParkingLot(capacity);
				for (int i = 1; i < commands.size(); i++) {
					String command = commands.get(i);
					String[] splitCmd = command.split(" ");
					if (splitCmd[0].equalsIgnoreCase("park")) {
						parkingLot.parkVehicle(new VehicleRegistration(splitCmd[1]));
					} else if (splitCmd[0].equalsIgnoreCase("leave")) {
						parkingLot.leave(splitCmd[1], splitCmd[2]);
					} else if (splitCmd[0].equalsIgnoreCase("status")) {
						parkingLot.dashboard();
					}
				}
			} else {
				logger.log(Level.SEVERE, "First command must be 'create_parking_lot'");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Check list of commands in input file");
		}
	}
}
