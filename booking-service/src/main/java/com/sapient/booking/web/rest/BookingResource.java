package com.sapient.booking.web.rest;



import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest Controller for managing {@link com.sapient.booking.data.domain.Booking} domain
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BookingResource {

    private final BookingService bookingResource;


    // Book movie tickets by selecting a theatre, timing, and preferred seats for the day – booking service
    // Done to make the request idempotent

    @PostMapping("/booking/{id}")
    public void bookTicket(@RequestBody @Valid BookingInfo bookingInfo, @PathVariable UUID id) {

        this.bookingResource.booking(bookingInfo, id);


    }





}
