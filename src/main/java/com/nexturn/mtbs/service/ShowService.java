package com.nexturn.mtbs.service;

import com.nexturn.mtbs.dto.response.SeatResponse;
import com.nexturn.mtbs.dto.response.ShowResponse;
import com.nexturn.mtbs.entity.Show;

import java.time.LocalDate;
import java.util.List;

public interface ShowService {

    Show createShow(Show show);

    ShowResponse getShowById(Long id);

    List<ShowResponse> getAllShows();

    List<ShowResponse> getShowsByMovieId(Long movieId);

    List<ShowResponse> getShowsByTheatreId(Long theatreId);

    List<ShowResponse> getShowsByMovieIdAndDate(
            Long movieId,
            LocalDate showDate
    );

    Show updateShow(Long id, Show show);

    void deleteShow(Long id);

    List<SeatResponse> getSeatsForShow(Long showId);
}