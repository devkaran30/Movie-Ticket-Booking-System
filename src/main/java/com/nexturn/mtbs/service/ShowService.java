package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Show;

import java.util.List;

public interface ShowService {

    Show createShow(Show show);

    Show getShowById(Long id);

    List<Show> getAllShows();

    Show updateShow(Long id, Show show);

    void deleteShow(Long id);
}