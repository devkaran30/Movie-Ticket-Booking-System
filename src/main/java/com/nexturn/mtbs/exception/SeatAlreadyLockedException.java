
package com.nexturn.mtbs.exception;

public class SeatAlreadyLockedException extends RuntimeException {

    public SeatAlreadyLockedException(String message) {
        super(message);
    }
}