package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.SeatResponse;
import com.nexturn.mtbs.dto.response.ShowResponse;
import com.nexturn.mtbs.entity.Show;
import com.nexturn.mtbs.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<List<ShowResponse>> getAllShows() {

        return ResponseEntity.ok(
                showService.getAllShows()
        );
    }

    // Get shows by movie ID
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowResponse>> getShowsByMovieId(
            @PathVariable Long movieId) {

        return ResponseEntity.ok(
                showService.getShowsByMovieId(movieId)
        );
    }
 // Get shows by movie ID and date
    @GetMapping("/movie/{movieId}/date/{showDate}")
    public ResponseEntity<List<ShowResponse>> getShowsByMovieIdAndDate(
            @PathVariable Long movieId,
            @PathVariable LocalDate showDate) {

        return ResponseEntity.ok(
                showService.getShowsByMovieIdAndDate(
                        movieId,
                        showDate
                )
        );
    }
 // Get shows by theatre ID
    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<ShowResponse>> getShowsByTheatreId(
            @PathVariable Long theatreId) {

        return ResponseEntity.ok(
                showService.getShowsByTheatreId(theatreId)
        );
    }

    // Get show by ID
    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> getShowById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                showService.getShowById(id)
        );
    }

    // Get seat availability for a show
    @GetMapping("/{showId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeatsForShow(
            @PathVariable Long showId) {

        return ResponseEntity.ok(
                showService.getSeatsForShow(showId)
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