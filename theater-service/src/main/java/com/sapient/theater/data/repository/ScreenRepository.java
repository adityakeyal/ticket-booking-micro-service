package com.sapient.theater.data.repository;

import com.sapient.theater.data.domain.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repository for {@link Screen} entity
 */
@Repository
public interface ScreenRepository extends JpaRepository<Screen, UUID> {

}
