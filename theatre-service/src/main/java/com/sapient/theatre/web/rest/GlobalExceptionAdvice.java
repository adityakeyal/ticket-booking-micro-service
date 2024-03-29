package com.sapient.theatre.web.rest;

import com.sapient.theatre.exception.InvalidInputException;
import com.sapient.theatre.exception.NoRecordFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Global message handling for {@link  InvalidInputException}
     *
     * @param exception - The instance of {@link  InvalidInputException}
     * @return HTTP Status Code - 400, Body - Message
     */
    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<String> exception(InvalidInputException exception) {

        return new ResponseEntity(this.messageSource.getMessage(exception.getMessage(), new Object[]{}, LocaleContextHolder.getLocale()),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }




}
