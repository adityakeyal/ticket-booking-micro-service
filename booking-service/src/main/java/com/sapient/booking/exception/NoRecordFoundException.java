package com.sapient.booking.exception;


/**
 * Used to indicate that no records were returned for the criteria
 */
public class NoRecordFoundException extends RuntimeException {

    public NoRecordFoundException() {
        super("Exception.notFound");
    }
}
