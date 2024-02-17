package com.sapient.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Record holding the Booking information. should contain the below data
 * @param showId - The ID of the show for which booking needs to be done
 * @param seatIds - The seats of the show which need to be blocked
 *
 */
public record BookingInfo(@NotNull(message = "NotNull.booking.showId") UUID showId, @NotNull(message = "NotNull.booking.seatIds") List<UUID> seatIds) {
}
