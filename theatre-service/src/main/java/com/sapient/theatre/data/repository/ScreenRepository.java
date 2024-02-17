package com.sapient.theatre.data.repository;

import com.sapient.theatre.data.domain.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repository for {@link Screen} entity
 */
@Repository
public interface ScreenRepository extends JpaRepository<Screen, UUID> {

}
