package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Screen;
import com.nexturn.mtbs.repository.ScreenRepository;
import com.nexturn.mtbs.service.ScreenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;

    public ScreenServiceImpl(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    @Override
    public Screen createScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    @Override
    public Screen getScreenById(Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Screen not found with id: " + id
                        ));
    }

    @Override
    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    @Override
    public Screen updateScreen(Long id, Screen screen) {

        Screen existingScreen = getScreenById(id);

        existingScreen.setTheatre(screen.getTheatre());
        existingScreen.setName(screen.getName());
        existingScreen.setTotalSeats(screen.getTotalSeats());
        existingScreen.setStatus(screen.getStatus());

        return screenRepository.save(existingScreen);
    }

    @Override
    public void deleteScreen(Long id) {

        Screen existingScreen = getScreenById(id);

        screenRepository.delete(existingScreen);
    }
}