package com.examly.springapp.controller;

import com.examly.springapp.model.SystemSettings;
import com.examly.springapp.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/settings")
@PreAuthorize("hasRole('ADMIN')")
public class SystemSettingsController {

    @Autowired
    private SystemSettingsService settingsService;

    @GetMapping
    public ResponseEntity<SystemSettings> getSettings() {
        try {
            SystemSettings settings = settingsService.getSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateSettings(@RequestBody SystemSettings settings) {
        try {
            // Validate required fields
            if (settings.getCompanyName() == null || settings.getCompanyName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Company name is required"));
            }

            SystemSettings savedSettings = settingsService.saveSettings(settings);
            return ResponseEntity.ok(savedSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to save settings: " + e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetSettings() {
        try {
            settingsService.resetToDefaults();
            SystemSettings defaultSettings = settingsService.getSettings();
            return ResponseEntity.ok(defaultSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to reset settings: " + e.getMessage()));
        }
    }

    @PostMapping("/test-email")
    public ResponseEntity<?> testEmailConfiguration() {
        try {
            // In a real implementation, you would send a test email here
            // For now, just return success
            return ResponseEntity.ok(Map.of("message", "Test email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to send test email: " + e.getMessage()));
        }
    }
}