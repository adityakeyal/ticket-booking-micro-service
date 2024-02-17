package com.sapient.booking.data.repository;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.data.domain.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for {@link com.sapient.booking.data.domain.BookingDetails} entity
 */
@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetails, UUID> {

}
