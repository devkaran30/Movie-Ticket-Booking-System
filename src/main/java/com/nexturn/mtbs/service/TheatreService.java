package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Theatre;

import java.util.List;

public interface TheatreService {

    Theatre createTheatre(Theatre theatre);

    Theatre getTheatreById(Long id);

    List<Theatre> getAllTheatres();

    Theatre updateTheatre(Long id, Theatre theatre);

    void deleteTheatre(Long id);
}