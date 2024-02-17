package com.sapient.theater.data.repository;

import com.sapient.theater.data.domain.TheaterOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link TheaterOwner} entity
 */
public interface TheaterOwnerRepository extends JpaRepository<TheaterOwner, UUID> {
}
