package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.BookingRequest;
import com.nexturn.mtbs.dto.response.BookingResponse;
import com.nexturn.mtbs.entity.Booking;
import com.nexturn.mtbs.repository.BookingSeatRepository;
import com.nexturn.mtbs.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingSeatRepository bookingSeatRepository;

    public BookingController(
            BookingService bookingService,
            BookingSeatRepository bookingSeatRepository) {

        this.bookingService = bookingService;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    // Convert Booking entity to BookingResponse
    private BookingResponse toBookingResponse(Booking booking) {

    	List<Long> seatIds = bookingSeatRepository
    	        .findByBookingId(booking.getId())
    	        .stream()
                .map(bookingSeat ->
                        bookingSeat.getSeat().getId())
                .collect(Collectors.toList());

        return new BookingResponse(
                booking.getId(),
                booking.getBookingNumber(),
                booking.getUser().getId(),
                booking.getShow().getId(),
                seatIds,
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getBookingTime()
        );
    }

    // Create booking
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody Booking booking) {

        Booking savedBooking =
                bookingService.createBooking(booking);

        return ResponseEntity.ok(
                toBookingResponse(savedBooking)
        );
    }

    // Create real booking
    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createRealBooking(
            @RequestBody BookingRequest bookingRequest) {

        Booking booking =
                bookingService.createRealBooking(bookingRequest);

        return ResponseEntity.ok(
                toBookingResponse(booking)
        );
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        List<BookingResponse> bookings =
                bookingService.getAllBookings()
                        .stream()
                        .map(this::toBookingResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(bookings);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) {

        Booking booking =
                bookingService.getBookingById(id);

        return ResponseEntity.ok(
                toBookingResponse(booking)
        );
    }

    // Update booking
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @RequestBody Booking booking) {

        Booking updatedBooking =
                bookingService.updateBooking(id, booking);

        return ResponseEntity.ok(
                toBookingResponse(updatedBooking)
        );
    }

    // Delete booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }
 // Cancel booking
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id) {

        Booking cancelledBooking =
                bookingService.cancelBooking(id);

        return ResponseEntity.ok(
                toBookingResponse(cancelledBooking)
        );
    }
}