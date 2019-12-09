package com.netcraker.controllers;

import com.netcraker.model.Settings;
import com.netcraker.services.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.OPTIONS})
@RequiredArgsConstructor
@RequestMapping("/api")
public class SettingsController {
    private final SettingsService settingsService;

    @GetMapping("/settings")
    public Settings getUserSettings() {
        return settingsService.getUserSettings();
    }

    @PutMapping("/settings")
    public void updateSettings(@RequestBody Settings settings) {
        settingsService.update(settings);
    }
}
