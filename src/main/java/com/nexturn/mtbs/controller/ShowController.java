package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.Show;
import com.nexturn.mtbs.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    // Create show
    @PostMapping
    public ResponseEntity<Show> createShow(
            @RequestBody Show show) {

        return ResponseEntity.ok(
                showService.createShow(show)
        );
    }

    // Get all shows
    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {

        return ResponseEntity.ok(
                showService.getAllShows()
        );
    }

    // Get show by ID
    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                showService.getShowById(id)
        );
    }

    // Update show
    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(
            @PathVariable Long id,
            @RequestBody Show show) {

        return ResponseEntity.ok(
                showService.updateShow(id, show)
        );
    }

    // Delete show
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long id) {

        showService.deleteShow(id);

        return ResponseEntity.noContent().build();
    }
}