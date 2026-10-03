package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Movie;

import java.util.List;

public interface MovieService {

    Movie createMovie(Movie movie);

    Movie getMovieById(Long id);

    List<Movie> getAllMovies();

    Movie updateMovie(Long id, Movie movie);

    void deleteMovie(Long id);
}