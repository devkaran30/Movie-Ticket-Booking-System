package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.Movie;
import com.nexturn.mtbs.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Create movie
    @PostMapping
    public ResponseEntity<Movie> createMovie(
            @RequestBody Movie movie) {

        return ResponseEntity.ok(
                movieService.createMovie(movie)
        );
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

    // Update movie
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @RequestBody Movie movie) {

        return ResponseEntity.ok(
                movieService.updateMovie(id, movie)
        );
    }

    // Delete movie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id) {

        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }
}