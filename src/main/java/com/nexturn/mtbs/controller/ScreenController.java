package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.Screen;
import com.nexturn.mtbs.service.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    // Create screen
    @PostMapping
    public ResponseEntity<Screen> createScreen(
            @RequestBody Screen screen) {

        return ResponseEntity.ok(
                screenService.createScreen(screen)
        );
    }

    // Get all screens
    @GetMapping
    public ResponseEntity<List<Screen>> getAllScreens() {

        return ResponseEntity.ok(
                screenService.getAllScreens()
        );
    }

    // Get screen by ID
    @GetMapping("/{id}")
    public ResponseEntity<Screen> getScreenById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                screenService.getScreenById(id)
        );
    }

    // Update screen
    @PutMapping("/{id}")
    public ResponseEntity<Screen> updateScreen(
            @PathVariable Long id,
            @RequestBody Screen screen) {

        return ResponseEntity.ok(
                screenService.updateScreen(id, screen)
        );
    }

    // Delete screen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(
            @PathVariable Long id) {

        screenService.deleteScreen(id);

        return ResponseEntity.noContent().build();
    }
}