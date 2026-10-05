
package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.MovieRequest;
import com.nexturn.mtbs.entity.Movie;
import com.nexturn.mtbs.enums.MovieStatus;
import com.nexturn.mtbs.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/movies")
public class AdminMovieController {

    private final MovieService movieService;

    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Get all movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {

        return ResponseEntity.ok(
                movieService.getAllMovies()
        );
    }

    // Get movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                movieService.getMovieById(id)
        );
    }

    // Create movie
    @PostMapping
    public ResponseEntity<Movie> createMovie(
            @RequestBody Movie movie) {

        return ResponseEntity.ok(
                movieService.createMovie(movie)
        );
    }

    // Update movie
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @RequestBody Movie movie) {

        return ResponseEntity.ok(
                movieService.updateMovie(id, movie)
        );
    }

    // Update movie status
    @PutMapping("/{id}/status")
    public ResponseEntity<Movie> updateMovieStatus(
            @PathVariable Long id,
            @RequestBody MovieRequest request) {

        Movie updatedMovie =
                movieService.updateMovieStatus(
                        id,
                        request.getStatus()
                );

        return ResponseEntity.ok(updatedMovie);
    }

    // Delete movie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id) {

        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }
}