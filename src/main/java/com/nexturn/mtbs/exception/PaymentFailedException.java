
package com.nexturn.mtbs.exception;

public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(String message) {
        super(message);
    }
}