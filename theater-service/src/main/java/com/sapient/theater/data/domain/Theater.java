package com.sapient.theater.data.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Entity for capturing the theater related information
 */
@Entity
@Table(name="THEATER")
@Data
public class Theater extends AbstractAuditingEntity {

    @Id
    private UUID id;
    @Column
    private String name;
    @Column private String city;
    @Column private String country;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=true)
    private TheaterOwner owner;

}
