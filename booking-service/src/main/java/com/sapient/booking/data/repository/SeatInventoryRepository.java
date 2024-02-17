package com.sapient.booking.data.repository;

import com.sapient.booking.data.domain.SeatInventory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link SeatInventory} entity
 *
 * Currently the Seats are locked in an optimistic lock. If needed we can change it to a pessimistic lock
 */
@Repository
public interface SeatInventoryRepository extends JpaRepository<SeatInventory, UUID> {
    @Override
    @Lock(LockModeType.OPTIMISTIC)
    List<SeatInventory> findAllById(Iterable<UUID> uuids);


}
