package com.sapient.booking.web.rest;

import com.sapient.booking.dto.ErrorResponse;
import com.sapient.booking.exception.NoRecordFoundException;
import com.sapient.booking.exception.UnableToLockSeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Global Exception Advice to handle exceptions thrown from RestControllers
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    private final MessageSource messageSource;

    /**
     * Global message handling for {@link  NoRecordFoundException}
     *
     * @param exception - The instance of {@link  NoRecordFoundException}
     * @return HTTP Status Code - 404, Body - Message
     */
    @ExceptionHandler(value = NoRecordFoundException.class)
    public ResponseEntity<String> exception(NoRecordFoundException exception) {
        return new ResponseEntity(this.messageSource.getMessage(exception.getMessage(), new Object[]{}, LocaleContextHolder.getLocale()),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Global message handling for {@link  UnableToLockSeatException}
     *
     * @param exception - The instance of {@link  UnableToLockSeatException}
     * @return HTTP Status Code - 422 , Body - JSON with code and message
     */
    @ExceptionHandler(value = UnableToLockSeatException.class)
    public ResponseEntity<ErrorResponse> exception(UnableToLockSeatException exception) {

        var errorResponse = new ErrorResponse(exception.getMessage(),this.messageSource.getMessage(exception.getMessage(), new Object[]{}, LocaleContextHolder.getLocale()));

        return new ResponseEntity(errorResponse,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }



    /**
     * Global message handling for {@link  UnableToLockSeatException}
     *
     * @param exception - The instance of {@link  UnableToLockSeatException}
     * @return HTTP Status Code - 422 , Body - JSON with code and message
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> exception(MethodArgumentNotValidException exception) {

        var errorResponse = new ErrorResponse(exception.getMessage(),exception.getMessage());

        return new ResponseEntity(errorResponse,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }






}
