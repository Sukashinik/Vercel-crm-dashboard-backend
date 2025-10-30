package com.examly.springapp.controller;

import com.examly.springapp.model.SecurityAlert;
import com.examly.springapp.model.UserSession;
import com.examly.springapp.repository.SecurityAlertRepository;
import com.examly.springapp.repository.UserSessionRepository;
import com.examly.springapp.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/security")
@PreAuthorize("hasRole('ADMIN')")
public class SecurityController {

    @Autowired
    private SecurityAlertRepository alertRepository;

    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    private SystemSettingsService settingsService;

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getSecurityMetrics() {
        try {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("activeSessions", sessionRepository.countByStatus("active"));
            metrics.put("securityAlerts", alertRepository.countByStatus(SecurityAlert.AlertStatus.ACTIVE));
            metrics.put("failedLogins", Math.random() * 20 + 5); // Mock data
            metrics.put("systemHealth", Math.random() * 20 + 80); // Mock data
            
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<SecurityAlert>> getSecurityAlerts() {
        try {
            List<SecurityAlert> alerts = alertRepository.findAllByOrderByCreatedAtDesc();
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<UserSession>> getActiveSessions() {
        try {
            List<UserSession> sessions = sessionRepository.findByStatusOrderByLastActiveDesc("active");
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/alerts/{id}/resolve")
    public ResponseEntity<?> resolveAlert(@PathVariable Long id) {
        try {
            SecurityAlert alert = alertRepository.findById(id).orElse(null);
            if (alert == null) {
                return ResponseEntity.notFound().build();
            }
            
            alert.setStatus(SecurityAlert.AlertStatus.RESOLVED);
            alert.setResolvedAt(LocalDateTime.now());
            alertRepository.save(alert);
            
            return ResponseEntity.ok(Map.of("message", "Alert resolved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to resolve alert"));
        }
    }

    @DeleteMapping("/alerts/{id}")
    public ResponseEntity<?> dismissAlert(@PathVariable Long id) {
        try {
            if (!alertRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            alertRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Alert dismissed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to dismiss alert"));
        }
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> terminateSession(@PathVariable String id) {
        try {
            if (!sessionRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            sessionRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Session terminated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to terminate session"));
        }
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateSecuritySettings(@RequestBody Map<String, Object> settings) {
        try {
            // Update security settings through SystemSettings
            var currentSettings = settingsService.getSettings();
            
            settings.forEach((key, value) -> {
                switch (key) {
                    case "twoFactorAuth":
                        currentSettings.setTwoFactorAuth((Boolean) value);
                        break;
                    case "sessionTimeout":
                        currentSettings.setSessionTimeout((Integer) value);
                        break;
                    case "passwordPolicy":
                        currentSettings.setRequireStrongPassword((Boolean) value);
                        break;
                    case "auditLogging":
                        // Handle audit logging setting
                        break;
                }
            });
            
            settingsService.saveSettings(currentSettings);
            return ResponseEntity.ok(Map.of("message", "Security settings updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update security settings"));
        }
    }
}