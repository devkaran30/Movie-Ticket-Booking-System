package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.ScreenRequest;
import com.nexturn.mtbs.entity.Screen;
import com.nexturn.mtbs.service.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/screens")
public class AdminScreenController {

    private final ScreenService screenService;

    public AdminScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping
    public ResponseEntity<List<Screen>> getAllScreens() {
        return ResponseEntity.ok(
                screenService.getAllScreens()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screen> getScreenById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                screenService.getScreenById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Screen> createScreen(
            @RequestBody Screen screen) {

        return ResponseEntity.ok(
                screenService.createScreen(screen)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Screen> updateScreen(
            @PathVariable Long id,
            @RequestBody Screen screen) {

        return ResponseEntity.ok(
                screenService.updateScreen(id, screen)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Screen> updateScreenStatus(
            @PathVariable Long id,
            @RequestBody ScreenRequest request) {

        Screen updatedScreen = screenService.updateScreenStatus(
                id,
                request.getStatus()
        );

        return ResponseEntity.ok(updatedScreen);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(
            @PathVariable Long id) {

        screenService.deleteScreen(id);

        return ResponseEntity.noContent().build();
    }
}