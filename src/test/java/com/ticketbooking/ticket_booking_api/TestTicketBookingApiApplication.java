package com.ticketbooking.ticket_booking_api;

import org.springframework.boot.SpringApplication;

public class TestTicketBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketBookingApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
