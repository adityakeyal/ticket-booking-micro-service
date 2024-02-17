package com.sapient.theater.data.domain;

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
    @JoinColumn(name="theater_id", nullable=false)
    private Theater theater;

    @Column
    private String name;

    @Column
    private String screenType;

    @Column
    private int totalSeating;
}
