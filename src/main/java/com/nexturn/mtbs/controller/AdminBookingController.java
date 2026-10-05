package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.BookingResponse;
import com.nexturn.mtbs.entity.Booking;
import com.nexturn.mtbs.repository.BookingSeatRepository;
import com.nexturn.mtbs.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingService bookingService;
    private final BookingSeatRepository bookingSeatRepository;

    public AdminBookingController(
            BookingService bookingService,
            BookingSeatRepository bookingSeatRepository) {

        this.bookingService = bookingService;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    private BookingResponse toBookingResponse(Booking booking) {

        List<Long> seatIds = bookingSeatRepository
                .findByBookingId(booking.getId())
                .stream()
                .map(bookingSeat -> bookingSeat.getSeat().getId())
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

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        List<BookingResponse> bookings =
                bookingService.getAllBookings()
                        .stream()
                        .map(this::toBookingResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) {

        Booking booking = bookingService.getBookingById(id);

        return ResponseEntity.ok(toBookingResponse(booking));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }
}