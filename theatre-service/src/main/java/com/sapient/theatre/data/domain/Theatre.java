package com.sapient.theatre.data.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Entity for capturing the theatre related information
 */
@Entity
@Table(name="THEATRE")
@Data
public class Theatre extends AbstractAuditingEntity {

    @Id
    private UUID id;
    @Column
    private String name;
    @Column private String city;
    @Column private String country;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=true)
    private TheatreOwner owner;

}
