package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Theatre;
import com.nexturn.mtbs.enums.TheatreStatus;
import com.nexturn.mtbs.repository.TheatreRepository;
import com.nexturn.mtbs.service.TheatreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;

    public TheatreServiceImpl(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @Override
    public Theatre createTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre getTheatreById(Long id) {
        return theatreRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Theatre not found with id: " + id
                        ));
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public Theatre updateTheatre(Long id, Theatre theatre) {

        Theatre existingTheatre = getTheatreById(id);

        existingTheatre.setName(theatre.getName());
        existingTheatre.setAddress(theatre.getAddress());
        existingTheatre.setCity(theatre.getCity());
        existingTheatre.setState(theatre.getState());
        existingTheatre.setPincode(theatre.getPincode());
        existingTheatre.setStatus(theatre.getStatus());

        return theatreRepository.save(existingTheatre);
    }

    @Override
    public Theatre updateTheatreStatus(
            Long id,
            TheatreStatus status) {

        Theatre existingTheatre = getTheatreById(id);

        existingTheatre.setStatus(status);

        return theatreRepository.save(existingTheatre);
    }

    @Override
    public void deleteTheatre(Long id) {

        Theatre existingTheatre = getTheatreById(id);

        theatreRepository.delete(existingTheatre);
    }
}