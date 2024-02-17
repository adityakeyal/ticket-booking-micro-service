package com.sapient.theatre.data.repository;

import com.sapient.theatre.data.domain.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link Theatre} entity
 */
public interface TheatreRepository extends JpaRepository<Theatre, UUID> {

}
