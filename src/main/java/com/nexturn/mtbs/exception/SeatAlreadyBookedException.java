
package com.nexturn.mtbs.exception;

public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}