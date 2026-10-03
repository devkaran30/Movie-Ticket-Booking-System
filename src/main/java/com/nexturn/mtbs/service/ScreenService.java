package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Screen;

import java.util.List;

public interface ScreenService {

    Screen createScreen(Screen screen);

    Screen getScreenById(Long id);

    List<Screen> getAllScreens();

    Screen updateScreen(Long id, Screen screen);

    void deleteScreen(Long id);
}