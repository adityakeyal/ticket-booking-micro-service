package com.sapient.booking.data.repository;

import com.sapient.booking.data.domain.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link Booking} entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {



}
