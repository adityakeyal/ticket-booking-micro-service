package com.sapient.booking.data.domain;

import com.sapient.booking.enums.BookingStatus;
import com.sapient.booking.enums.SeatAvailability;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;


/**
 * Entity for capturing the show related information
 */
@Entity
@Data
@Table(name = "SEAT_INVENTORY")
public class SeatInventory extends AbstractAuditingEntity {

    @Id
    private UUID id;

    @Column
    private UUID showId;

    @Column
    private BigDecimal price;

    @Column
    private String seat;

    @Column
    private Integer rowNo;

    @Column
    @Enumerated(EnumType.STRING)
    private SeatAvailability availability;

    @Version
    @Column
    private Integer version;


}
