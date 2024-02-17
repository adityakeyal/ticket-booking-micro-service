package com.sapient.theater.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;

/**
 * Entity for theater owner related information
 */
@Entity
@Table(name="THEATER_OWNER")
@Data
public class TheaterOwner extends AbstractAuditingEntity {

    @Id
    private UUID id;

    @Column(name="owner_name")
    private String name;


}
