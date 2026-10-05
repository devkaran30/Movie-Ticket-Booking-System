package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Screen;
import com.nexturn.mtbs.enums.ScreenStatus;

import java.util.List;

public interface ScreenService {

    Screen createScreen(Screen screen);

    Screen getScreenById(Long id);

    List<Screen> getAllScreens();

    Screen updateScreen(Long id, Screen screen);

    Screen updateScreenStatus(Long id, ScreenStatus status);

    void deleteScreen(Long id);
}