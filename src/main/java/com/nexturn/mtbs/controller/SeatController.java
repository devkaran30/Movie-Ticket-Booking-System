package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.Seat;
import com.nexturn.mtbs.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Create seat
    @PostMapping
    public ResponseEntity<Seat> createSeat(
            @RequestBody Seat seat) {

        return ResponseEntity.ok(
                seatService.createSeat(seat)
        );
    }

    // Get all seats
    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {

        return ResponseEntity.ok(
                seatService.getAllSeats()
        );
    }

    // Get seat by ID
    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                seatService.getSeatById(id)
        );
    }

    // Update seat
    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(
            @PathVariable Long id,
            @RequestBody Seat seat) {

        return ResponseEntity.ok(
                seatService.updateSeat(id, seat)
        );
    }

    // Delete seat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(
            @PathVariable Long id) {

        seatService.deleteSeat(id);

        return ResponseEntity.noContent().build();
    }
}