package com.sapient.booking.exception;


/**
 * Used to indicate that no records were returned for the criteria
 */
public class MultipleRecordsFoundException extends RuntimeException {

    public MultipleRecordsFoundException() {
        this("Exception.multipleRecordFound");
    }

    public MultipleRecordsFoundException(String message) {
        super(message);
    }


}
