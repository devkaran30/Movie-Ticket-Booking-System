package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Movie;
import com.nexturn.mtbs.enums.MovieStatus;

import java.util.List;

public interface MovieService {

    Movie createMovie(Movie movie);

    Movie getMovieById(Long id);

    List<Movie> getAllMovies();

    List<Movie> getMoviesByStatus(MovieStatus status);

    List<Movie> searchMoviesByTitle(String title);

    Movie updateMovie(Long id, Movie movie);

    Movie updateMovieStatus(Long id, MovieStatus status);

    void deleteMovie(Long id);
}