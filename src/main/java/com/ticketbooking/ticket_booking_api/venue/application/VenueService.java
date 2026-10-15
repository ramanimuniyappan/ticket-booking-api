package com.ticketbooking.ticket_booking_api.venue.application;

import com.ticketbooking.ticket_booking_api.venue.api.dto.request.CreateVenueRequest;
import com.ticketbooking.ticket_booking_api.venue.api.dto.response.VenueResponse;
import com.ticketbooking.ticket_booking_api.venue.domain.exception.VenueNotFoundException;
import com.ticketbooking.ticket_booking_api.venue.persistence.VenueEntity;
import com.ticketbooking.ticket_booking_api.venue.persistence.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Transactional
    public VenueResponse createVenue(CreateVenueRequest request) {
        VenueEntity venue = new VenueEntity(request.name(), request.city(),
                request.address(), request.capacity());

        VenueEntity saved = venueRepository.save(venue);
        return VenueResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public VenueResponse getVenueById(Long venueId) {
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        return VenueResponse.from(venue);
    }
}
