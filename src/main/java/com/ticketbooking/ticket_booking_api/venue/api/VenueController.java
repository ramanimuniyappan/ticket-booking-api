package com.ticketbooking.ticket_booking_api.venue.api;

import com.ticketbooking.ticket_booking_api.venue.api.dto.request.CreateVenueRequest;
import com.ticketbooking.ticket_booking_api.venue.api.dto.response.VenueResponse;
import com.ticketbooking.ticket_booking_api.venue.application.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody CreateVenueRequest request) {
        VenueResponse response = venueService.createVenue(request);

        URI location = URI.create("/api/v1/venues/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        VenueResponse response = venueService.getVenueById(id);
        return ResponseEntity.ok(response);
    }
}
