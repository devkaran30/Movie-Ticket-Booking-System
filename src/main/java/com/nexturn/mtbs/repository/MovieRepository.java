package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}