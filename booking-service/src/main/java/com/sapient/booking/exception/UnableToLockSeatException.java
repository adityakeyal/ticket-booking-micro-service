package com.sapient.booking.exception;

/**
 *  Exception throws when we are unable to lock a seat for booking
 */
public class UnableToLockSeatException extends RuntimeException {


    public UnableToLockSeatException(String message){
        super(message);
    }


}
