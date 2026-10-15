package com.ticketbooking.ticket_booking_api.venue.domain.exception;

public class VenueNotFoundException extends RuntimeException {
    private final Long venueId;
    public VenueNotFoundException(Long venueId) {
        super("Venue not found with id: " + venueId);
        this.venueId = venueId;
    }

    public Long venueId() {
        return venueId;
    }
}
