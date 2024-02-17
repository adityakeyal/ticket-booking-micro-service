package com.sapient.theatre.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;

/**
 * Entity for theatre owner related information
 */
@Entity
@Table(name="THEATRE_OWNER")
@Data
public class TheatreOwner extends AbstractAuditingEntity {

    @Id
    private UUID id;

    @Column(name="owner_name")
    private String name;


}
