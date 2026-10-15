
package com.ticketbooking.ticket_booking_api.venue.persistence;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "venues")
@EntityListeners(AuditingEntityListener.class)
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected VenueEntity() {
        // Required by JPA
    }

    public VenueEntity(
            String name,
            String city,
            String address,
            int capacity) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Venue name cannot be blank"
            );
        }

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City cannot be blank"
            );
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Address cannot be blank"
            );
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be positive"
            );
        }

        this.name = name;
        this.city = city;
        this.address = address;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
