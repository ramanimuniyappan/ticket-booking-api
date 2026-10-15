package com.ticketbooking.ticket_booking_api.venue.api.dto.response;

import com.ticketbooking.ticket_booking_api.venue.persistence.VenueEntity;

import java.time.Instant;

public record VenueResponse(
        Long id,
        String name,
        String city,
        String address,
        int capacity,
        Instant createdAt,
        Instant updatedAt
) {
    public static VenueResponse from(VenueEntity venue) {
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getAddress(),
                venue.getCapacity(),
                venue.getCreatedAt(),
                venue.getUpdatedAt()
        );
    }
}
