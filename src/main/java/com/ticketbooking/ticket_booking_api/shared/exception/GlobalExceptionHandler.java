package com.ticketbooking.ticket_booking_api.shared.exception;

import com.ticketbooking.ticket_booking_api.venue.domain.exception.SeatAlreadyExistsException;
import com.ticketbooking.ticket_booking_api.venue.domain.exception.VenueNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.computeIfAbsent(
                        error.getField(),
                        key -> new ArrayList<>()
                ).add(error.getDefaultMessage())
        );

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Validation failed");
        problem.setDetail("One or more request fields are invalid");
        problem.setProperty("errors", errors);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(VenueNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleVenueNotFound(VenueNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pd.setTitle("Venue Not Found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("venueId", ex.venueId());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(SeatAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleSeatAlreadyExists(
            SeatAlreadyExistsException ex) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        pd.setTitle("Seat already exists");
        pd.setDetail(ex.getMessage());
        pd.setProperty("seatLabel", ex.seatLabel());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(pd);
    }
}
