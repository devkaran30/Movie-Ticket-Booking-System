package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.SeatRequest;
import com.nexturn.mtbs.entity.Seat;
import com.nexturn.mtbs.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/seats")
public class AdminSeatController {

    private final SeatService seatService;

    public AdminSeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(
                seatService.getAllSeats()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                seatService.getSeatById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(
            @RequestBody Seat seat) {

        return ResponseEntity.ok(
                seatService.createSeat(seat)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(
            @PathVariable Long id,
            @RequestBody Seat seat) {

        return ResponseEntity.ok(
                seatService.updateSeat(id, seat)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Seat> updateSeatStatus(
            @PathVariable Long id,
            @RequestBody SeatRequest request) {

        Seat updatedSeat = seatService.updateSeatStatus(
                id,
                request.getStatus()
        );

        return ResponseEntity.ok(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(
            @PathVariable Long id) {

        seatService.deleteSeat(id);

        return ResponseEntity.noContent().build();
    }
}