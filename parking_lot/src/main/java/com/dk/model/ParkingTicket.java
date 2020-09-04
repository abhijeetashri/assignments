package com.dk.model;

import com.dk.service.StorageService;

public class ParkingTicket {

	private Long ticketId;
	private Integer slotNumber;
	private long issuedAt;

	public ParkingTicket(Integer slotNumber) {
		this.issuedAt = System.currentTimeMillis();
		this.slotNumber = slotNumber;
		this.ticketId = StorageService.getInstance().getNextTicketNumber();
	}

	public Long getTicketId() {
		return ticketId;
	}

	public Integer getSlotNumber() {
		return slotNumber;
	}

	public long getIssuedAt() {
		return issuedAt;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof ParkingTicket))
			return false;

		return (this.getTicketId() == ((ParkingTicket) obj).getTicketId())
				&& (this.getSlotNumber().compareTo(((ParkingTicket) obj).getSlotNumber()) == 0);
	}

	@Override
	public int hashCode() {
		if (this.getTicketId() == null)
			return 0;
		return this.getTicketId().intValue();
	}
}