package com.sapient.theatre.exception;


/**
 * Used to indicate that no records were returned for the criteria
 */
public class NoRecordFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoRecordFoundException() {
        super("Exception.notFound");
    }
}
