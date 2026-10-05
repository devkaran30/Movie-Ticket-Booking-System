package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Movie;
import com.nexturn.mtbs.enums.MovieStatus;
import com.nexturn.mtbs.repository.MovieRepository;
import com.nexturn.mtbs.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Movie not found with id: " + id
                        ));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByStatus(MovieStatus status) {
        return movieRepository.findByStatus(status);
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Movie updateMovie(Long id, Movie movie) {

        Movie existingMovie = getMovieById(id);

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setLanguage(movie.getLanguage());
        existingMovie.setGenre(movie.getGenre());
        existingMovie.setDurationMinutes(movie.getDurationMinutes());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        existingMovie.setCertificate(movie.getCertificate());
        existingMovie.setPosterUrl(movie.getPosterUrl());
        existingMovie.setMovieFormat(movie.getMovieFormat());
        existingMovie.setStatus(movie.getStatus());

        return movieRepository.save(existingMovie);
    }

    @Override
    public Movie updateMovieStatus(Long id, MovieStatus status) {

        Movie existingMovie = getMovieById(id);

        existingMovie.setStatus(status);

        return movieRepository.save(existingMovie);
    }

    @Override
    public void deleteMovie(Long id) {

        Movie existingMovie = getMovieById(id);

        movieRepository.delete(existingMovie);
    }
}