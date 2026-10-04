package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.SeatLock;
import com.nexturn.mtbs.service.SeatLockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-locks")
public class SeatLockController {

    private final SeatLockService seatLockService;

    public SeatLockController(SeatLockService seatLockService) {
        this.seatLockService = seatLockService;
    }

    @PostMapping
    public ResponseEntity<SeatLock> createSeatLock(
            @RequestBody SeatLock seatLock) {

        return ResponseEntity.ok(
                seatLockService.lockSeat(seatLock)
        );
    }

    @GetMapping
    public ResponseEntity<List<SeatLock>> getAllSeatLocks() {

        return ResponseEntity.ok(
                seatLockService.getAllSeatLocks()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatLock> getSeatLockById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                seatLockService.getSeatLockById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatLock> updateSeatLock(
            @PathVariable Long id,
            @RequestBody SeatLock seatLock) {

        return ResponseEntity.ok(
                seatLockService.updateSeatLock(id, seatLock)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatLock(
            @PathVariable Long id) {

        seatLockService.deleteSeatLock(id);

        return ResponseEntity.noContent().build();
    }
}