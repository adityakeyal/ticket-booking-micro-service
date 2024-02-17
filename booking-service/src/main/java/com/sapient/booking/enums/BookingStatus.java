package com.sapient.booking.enums;

/**
 * The Booking Status. This can be in the below states
 *  - PAYMENT_PENDING - Initial Status, we wait for x mins to confirm
 *  - CONFIRMED - If the booking is confirmed (after payment is successful)
 *  - REJECTED - If the payment is rejected or no confirmation received in X mins
 */
public enum BookingStatus {

    REJECTED, CONFIRMED, PAYMENT_PENDING

}
