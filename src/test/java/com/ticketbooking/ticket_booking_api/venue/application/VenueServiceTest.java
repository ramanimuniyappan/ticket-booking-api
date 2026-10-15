
package com.ticketbooking.ticket_booking_api.venue.application;

import com.ticketbooking.ticket_booking_api.venue.api.dto.request.CreateVenueRequest;
import com.ticketbooking.ticket_booking_api.venue.api.dto.response.VenueResponse;
import com.ticketbooking.ticket_booking_api.venue.domain.exception.VenueNotFoundException;
import com.ticketbooking.ticket_booking_api.venue.persistence.VenueEntity;
import com.ticketbooking.ticket_booking_api.venue.persistence.VenueRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    @Test
    void shouldCreateVenueSuccessfully() {
        CreateVenueRequest request = new CreateVenueRequest("Chennai Arena",
                "Chennai", "Main Road", 100);
        VenueEntity savedVenue = new VenueEntity("Chennai Arena",
                "Chennai", "Main Road", 100);
        when(venueRepository.save(any(VenueEntity.class)))
                .thenReturn(savedVenue);

        VenueResponse response = venueService.createVenue(request);

        assertNotNull(response);
        assertEquals("Chennai Arena", response.name());
        assertEquals("Chennai", response.city());
        assertEquals("Main Road", response.address());
        assertEquals(100, response.capacity());

        ArgumentCaptor<VenueEntity> captor =
                ArgumentCaptor.forClass(VenueEntity.class);

        verify(venueRepository).save(captor.capture());

        VenueEntity capturedVenue = captor.getValue();

        assertEquals("Chennai Arena", capturedVenue.getName());
        assertEquals("Chennai", capturedVenue.getCity());
        assertEquals("Main Road", capturedVenue.getAddress());
        assertEquals(100, capturedVenue.getCapacity());
    }


    @Test
    void shouldPropagateExceptionWhenRepositorySaveFails() {
        CreateVenueRequest request = new CreateVenueRequest("Chennai Arena",
                "Chennai", "Main Road", 100);

        when(venueRepository.save(any(VenueEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Database constraint violated"));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> venueService.createVenue(request));

        assertEquals("Database constraint violated", exception.getMessage());
        verify(venueRepository, times(1)).save(any(VenueEntity.class));
    }


    @Test
    void shouldReturnVenueWhenIdExists() {
        Long venueId = 10L;
        VenueEntity venue = new VenueEntity("Chennai Arena", "Chennai", "Main Road", 100);
        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));

        VenueResponse response = venueService.getVenueById(venueId);

        assertNotNull(response);
        assertEquals("Chennai Arena", response.name());
        assertEquals(100, response.capacity());

        verify(venueRepository).findById(venueId);
    }

    @Test
    void shouldThrowVenueNotFoundWhenIdDoesNotExist() {
        Long venueId = 999L;

        when(venueRepository.findById(venueId)).thenReturn(Optional.empty());

        VenueNotFoundException exception = assertThrows(VenueNotFoundException.class,
                () -> venueService.getVenueById(venueId));
        assertEquals("Venue not found with id: " + venueId, exception.getMessage());
        verify(venueRepository).findById(venueId);
    }
}
