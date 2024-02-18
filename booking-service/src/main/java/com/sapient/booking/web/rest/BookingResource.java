package com.sapient.booking.web.rest;



import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.dto.ErrorResponse;
import com.sapient.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest Controller for managing {@link com.sapient.booking.data.domain.Booking} domain
 */

@Tag(name = "Booking", description = "API related to Booking")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BookingResource {

    private final BookingService bookingResource;

    /**
     * The method is used to create a booking. This is idempotent by design.
     * The process of booking is as follows:
     *
     * <ul>
     *     <li>Provide the show and seats to be booked</li>
     *     <li>If the request is valid and seats available API will create a booking</li>
     *     <li>If the booking already exists, then return the same booking</li>
     * </ul>
     *
     * @param bookingInfo - the information used to create a booking
     * @param id - The id of the booking
     * @return
     */

    @Operation(
            summary = "Book a ticket for a show",
            description = "books a ticket for a show and returns the booking details. the API is idempotent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "invalid body content" ,  content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "invalid body content" ,  content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "422", description = "invalid body content" ,  content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/booking/{id}")
    public Booking bookTicket(@PathVariable UUID id, @RequestBody @Valid BookingInfo bookingInfo) {
        return this.bookingResource.booking(id, bookingInfo);
    }


}
