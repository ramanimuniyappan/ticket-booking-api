package com.ticketbooking.ticket_booking_api.venue.domain.exception;

public class SeatAlreadyExistsException extends RuntimeException {

    private final String seatLabel;

    public SeatAlreadyExistsException(String seatLabel) {
        super("Seat already exists: " + seatLabel);
        this.seatLabel = seatLabel;
    }

    public String seatLabel() {
        return seatLabel;
    }
}
