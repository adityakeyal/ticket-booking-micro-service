package com.sapient.theater.data.repository;

import com.sapient.theater.data.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link Theater} entity
 */
public interface TheaterRepository extends JpaRepository<Theater, UUID> {

}
