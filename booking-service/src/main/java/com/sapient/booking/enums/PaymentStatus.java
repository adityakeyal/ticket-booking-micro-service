package com.sapient.booking.enums;

/**
 * The Payment Status of a booking
 *  - PENDING - Still awaiting confirmation on payment
 *  - CONFIRMED - Payment is confirmed
 *  - REJECTED - Payment is rejected/cancelled
 */
public enum PaymentStatus {
    PENDING, CONFIRMED, REJECTED
}
