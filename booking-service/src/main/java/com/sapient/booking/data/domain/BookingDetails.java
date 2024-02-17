package com.sapient.booking.data.domain;

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
@Table(name="BOOKING_DETAILS")
public class BookingDetails extends AbstractAuditingEntity {

    public BookingDetails(){
        id = UUID.randomUUID();
    }
    public BookingDetails(SeatInventory seat){
        System.out.println(seat);
        this.id = UUID.randomUUID();
        this.seat = seat;
    }

    @Id
    private UUID id;

    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL)
    private SeatInventory seat;



}
