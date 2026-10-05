package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Theatre;
import com.nexturn.mtbs.enums.TheatreStatus;

import java.util.List;

public interface TheatreService {

    Theatre createTheatre(Theatre theatre);

    Theatre getTheatreById(Long id);

    List<Theatre> getAllTheatres();

    Theatre updateTheatre(Long id, Theatre theatre);

    Theatre updateTheatreStatus(Long id, TheatreStatus status);

    void deleteTheatre(Long id);
}