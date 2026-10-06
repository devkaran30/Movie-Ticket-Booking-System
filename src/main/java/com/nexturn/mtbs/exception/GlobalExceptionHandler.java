package com.nexturn.mtbs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(String message) { }

    @ExceptionHandler({
            BookingNotFoundException.class,
            MovieNotFoundException.class,
            PaymentNotFoundException.class,
            RefundNotFoundException.class,
            ScreenNotFoundException.class,
            SeatNotFoundException.class,
            ShowNotFoundException.class,
            TheatreNotFoundException.class,
            UserNotFoundException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({
            SeatAlreadyBookedException.class,
            SeatAlreadyLockedException.class,
            BookingCancellationException.class,
            PaymentFailedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        var errors = new LinkedHashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        return response(HttpStatus.BAD_REQUEST, errors.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedRequest(
            HttpMessageNotReadableException exception
    ) {
        return response(HttpStatus.BAD_REQUEST, "Request body is missing or malformed.");
    }

    // Handles RuntimeExceptions thrown by services that have not yet been
    // replaced with a more specific domain exception.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException exception
    ) {
        var message = exception.getMessage();
        return response(HttpStatus.BAD_REQUEST,
                message == null || message.isBlank() ? "Request could not be processed." : message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        var message = exception.getMessage();
        return response(HttpStatus.INTERNAL_SERVER_ERROR,
                message == null || message.isBlank()
                        ? "An unexpected error occurred. Please try again later."
                        : message);
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(message));
    }
}


