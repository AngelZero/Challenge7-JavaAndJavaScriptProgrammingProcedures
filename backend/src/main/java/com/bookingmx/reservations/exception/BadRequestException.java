package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Signals a 400 Bad Request due to invalid input or state. */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String m) { super(m); }
}
