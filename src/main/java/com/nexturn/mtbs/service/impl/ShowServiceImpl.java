package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Show;
import com.nexturn.mtbs.repository.ShowRepository;
import com.nexturn.mtbs.service.ShowService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    public ShowServiceImpl(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @Override
    public Show createShow(Show show) {
        return showRepository.save(show);
    }

    @Override
    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Show not found with id: " + id
                        ));
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    public Show updateShow(Long id, Show show) {

        Show existingShow = getShowById(id);

        existingShow.setMovie(show.getMovie());
        existingShow.setScreen(show.getScreen());
        existingShow.setShowDate(show.getShowDate());
        existingShow.setStartTime(show.getStartTime());
        existingShow.setEndTime(show.getEndTime());
        existingShow.setTicketPrice(show.getTicketPrice());
        existingShow.setStatus(show.getStatus());

        return showRepository.save(existingShow);
    }

    @Override
    public void deleteShow(Long id) {

        Show existingShow = getShowById(id);

        showRepository.delete(existingShow);
    }
}