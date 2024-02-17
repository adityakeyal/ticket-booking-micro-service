package com.sapient.booking.enums;

/**
 * Indicates the different states for Seat Availability
 *  - AVAILABLE - The seat is available to be booked
 *  - BLOCKED - The seat has been blocked pending a user payment confirmation/rejection
 *  - BOOKED - The seat is booked
 *  - UNAVAILABLE - The seat is unavailable for some reason (operation done by owner/admin)
 */
public enum SeatAvailability {

    AVAILABLE, BLOCKED, BOOKED, UNAVAILABLE
}
