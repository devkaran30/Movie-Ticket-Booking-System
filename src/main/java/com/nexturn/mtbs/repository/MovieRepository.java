package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Movie;
import com.nexturn.mtbs.enums.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByStatus(MovieStatus status);
}