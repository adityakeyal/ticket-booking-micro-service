package com.sapient.theatre.data.repository;

import com.sapient.theatre.data.domain.TheatreOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link TheatreOwner} entity
 */
public interface TheatreOwnerRepository extends JpaRepository<TheatreOwner, UUID> {
}
