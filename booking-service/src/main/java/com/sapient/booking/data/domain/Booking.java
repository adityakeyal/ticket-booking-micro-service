package com.sapient.booking.data.domain;

import com.sapient.booking.enums.BookingStatus;
import com.sapient.booking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * Entity for capturing the screen related information
 */
@Entity
@Data
@Table(name="BOOKING")
public class Booking extends AbstractAuditingEntity {

    @Id
    private UUID id;

    @Column
    private UUID showId;

    @Column
    private UUID userId;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = true)
    private UUID paymentId;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "booking_id")
    private Set<BookingDetails> details;

}
