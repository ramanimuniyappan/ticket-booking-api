package com.ticketbooking.ticket_booking_api.venue.api;

import com.ticketbooking.ticket_booking_api.shared.exception.GlobalExceptionHandler;
import com.ticketbooking.ticket_booking_api.venue.api.dto.request.CreateVenueRequest;
import com.ticketbooking.ticket_booking_api.venue.api.dto.response.VenueResponse;
import com.ticketbooking.ticket_booking_api.venue.application.VenueService;
import com.ticketbooking.ticket_booking_api.venue.domain.exception.VenueNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenueController.class)
@Import(GlobalExceptionHandler.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VenueService venueService;

    @Test
    void shouldRejectInvalidVenueRequest() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "city": "Chennai",
                  "address": "Main Road",
                  "capacity": -10
                }
                """;

        mockMvc.perform(post("/api/v1/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Venue name is required"))
                .andExpect(jsonPath("$.errors.capacity")
                        .value("Capacity must be greater than zero"));

        verifyNoInteractions(venueService);
    }


    @Test
    void shouldCreateVenueAndReturn201() throws Exception {

        // Arrange
        String requestBody = """
                {
                  "name": "Chennai Arena",
                  "city": "Chennai",
                  "address": "Main Road",
                  "capacity": 100
                }
                """;

        VenueResponse expectedResponse = new VenueResponse(
                1L, "Chennai Arena", "Chennai", "Main Road", 100,
                Instant.parse("2026-10-16T10:00:00Z"),
                Instant.parse("2026-10-16T10:00:00Z")
        );

        when(venueService.createVenue(any(CreateVenueRequest.class))).thenReturn(expectedResponse);

        // Act and assert HTTP response
        mockMvc.perform(
                        post("/api/v1/venues")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location", "/api/v1/venues/1"
                ))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Chennai Arena"))
                .andExpect(jsonPath("$.city").value("Chennai"))
                .andExpect(jsonPath("$.address").value("Main Road"))
                .andExpect(jsonPath("$.capacity").value(100));

        // Verify controller-to-service interaction
        ArgumentCaptor<CreateVenueRequest> captor =
                ArgumentCaptor.forClass(CreateVenueRequest.class);

        verify(venueService).createVenue(captor.capture());

        CreateVenueRequest capturedRequest = captor.getValue();

        assertEquals("Chennai Arena", capturedRequest.name());
        assertEquals("Chennai", capturedRequest.city());
        assertEquals("Main Road", capturedRequest.address());
        assertEquals(100, capturedRequest.capacity());
    }


    @Test
    void shouldReturnVenueWhenIdExists() throws Exception {
        VenueResponse response = new VenueResponse(
                10L, "Chennai Arena", "Chennai", "Main Road", 100,
                Instant.parse("2026-10-16T10:00:00Z"), Instant.parse("2026-10-16T10:00:00Z"));

        when(venueService.getVenueById(10L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/venues/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Chennai Arena"))
                .andExpect(jsonPath("$.capacity").value(100));

        verify(venueService).getVenueById(10L);
    }


    @Test
    void shouldReturn404WhenVenueDoesNotExist() throws Exception {
        Long venueId = 999L;
        when(venueService.getVenueById(venueId)).thenThrow(new VenueNotFoundException(venueId));

        mockMvc.perform(get("/api/v1/venues/{id}", venueId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Venue Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail")
                        .value("Venue not found with id: " + venueId))
                .andExpect(jsonPath("$.venueId").value(venueId));

        verify(venueService).getVenueById(venueId);
    }

}
