package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.BookingSeat;
import com.nexturn.mtbs.service.BookingSeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-seats")
public class BookingSeatController {

    private final BookingSeatService bookingSeatService;

    public BookingSeatController(BookingSeatService bookingSeatService) {
        this.bookingSeatService = bookingSeatService;
    }

    @PostMapping
    public ResponseEntity<BookingSeat> createBookingSeat(
            @RequestBody BookingSeat bookingSeat) {

        return ResponseEntity.ok(
                bookingSeatService.createBookingSeat(bookingSeat)
        );
    }

    @GetMapping
    public ResponseEntity<List<BookingSeat>> getAllBookingSeats() {

        return ResponseEntity.ok(
                bookingSeatService.getAllBookingSeats()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingSeat> getBookingSeatById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingSeatService.getBookingSeatById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingSeat> updateBookingSeat(
            @PathVariable Long id,
            @RequestBody BookingSeat bookingSeat) {

        return ResponseEntity.ok(
                bookingSeatService.updateBookingSeat(id, bookingSeat)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingSeat(
            @PathVariable Long id) {

        bookingSeatService.deleteBookingSeat(id);

        return ResponseEntity.noContent().build();
    }
}