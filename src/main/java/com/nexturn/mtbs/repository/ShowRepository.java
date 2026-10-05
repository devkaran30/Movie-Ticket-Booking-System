package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByScreenTheatreId(Long theatreId);

    List<Show> findByMovieIdAndShowDate(
            Long movieId,
            LocalDate showDate
    );
}