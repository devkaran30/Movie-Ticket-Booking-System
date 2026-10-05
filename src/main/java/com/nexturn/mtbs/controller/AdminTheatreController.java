package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.TheatreRequest;
import com.nexturn.mtbs.entity.Theatre;
import com.nexturn.mtbs.service.TheatreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/theatres")
public class AdminTheatreController {

    private final TheatreService theatreService;

    public AdminTheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    // Get all theatres
    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {

        return ResponseEntity.ok(
                theatreService.getAllTheatres()
        );
    }

    // Get theatre by ID
    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                theatreService.getTheatreById(id)
        );
    }

    // Create theatre
    @PostMapping
    public ResponseEntity<Theatre> createTheatre(
            @RequestBody Theatre theatre) {

        return ResponseEntity.ok(
                theatreService.createTheatre(theatre)
        );
    }

    // Update theatre
    @PutMapping("/{id}")
    public ResponseEntity<Theatre> updateTheatre(
            @PathVariable Long id,
            @RequestBody Theatre theatre) {

        return ResponseEntity.ok(
                theatreService.updateTheatre(id, theatre)
        );
    }

    // Update theatre status
    @PutMapping("/{id}/status")
    public ResponseEntity<Theatre> updateTheatreStatus(
            @PathVariable Long id,
            @RequestBody TheatreRequest request) {

        Theatre updatedTheatre =
                theatreService.updateTheatreStatus(
                        id,
                        request.getStatus()
                );

        return ResponseEntity.ok(updatedTheatre);
    }

    // Delete theatre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(
            @PathVariable Long id) {

        theatreService.deleteTheatre(id);

        return ResponseEntity.noContent().build();
    }
}