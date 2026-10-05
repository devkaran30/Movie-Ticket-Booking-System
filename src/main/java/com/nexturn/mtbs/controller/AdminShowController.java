package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.ShowResponse;
import com.nexturn.mtbs.entity.Show;
import com.nexturn.mtbs.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/shows")
public class AdminShowController {

    private final ShowService showService;

    public AdminShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public ResponseEntity<List<ShowResponse>> getAllShows() {
        return ResponseEntity.ok(
                showService.getAllShows()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> getShowById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                showService.getShowById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Show> createShow(
            @RequestBody Show show) {

        return ResponseEntity.ok(
                showService.createShow(show)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(
            @PathVariable Long id,
            @RequestBody Show show) {

        return ResponseEntity.ok(
                showService.updateShow(id, show)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long id) {

        showService.deleteShow(id);

        return ResponseEntity.noContent().build();
    }
}