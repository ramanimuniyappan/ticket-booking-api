package com.ticketbooking.ticket_booking_api.venue.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateVenueRequest(
        @NotBlank(message = "Venue name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be greater than zero")
        Integer capacity
) {
}
