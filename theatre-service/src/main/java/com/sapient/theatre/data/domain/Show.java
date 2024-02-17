package com.sapient.theatre.data.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Entity for capturing the show related information
 */
@Entity
@Data
@Table(name = "SHOW")
public class Show extends AbstractAuditingEntity {

    @Id
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
    @Column
    @Temporal(TemporalType.DATE)
    private LocalDate playingOn;
    @Column
    private LocalDateTime startTime;
    @Column
    private UUID movieId;
    @Column
    private String availability;

    @Column
    private String status;

}
