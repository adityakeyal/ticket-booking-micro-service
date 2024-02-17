package com.sapient.theatre.exception;


/**
 * Used to indicate that invalid input was provided
 */
public class InvalidInputException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException() {
        super("Exception.dataInvalid");
    }
}
