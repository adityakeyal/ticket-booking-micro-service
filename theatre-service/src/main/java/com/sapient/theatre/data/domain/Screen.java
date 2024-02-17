package com.sapient.theatre.data.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Entity for capturing the screen related information
 */
@Entity
@Data
@Table(name="SCREEN")
public class Screen extends AbstractAuditingEntity{

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name="theatre_id", nullable=false)
    private Theatre theatre;

    @Column
    private String name;

    @Column
    private String screenType;

    @Column
    private int totalSeating;
}
