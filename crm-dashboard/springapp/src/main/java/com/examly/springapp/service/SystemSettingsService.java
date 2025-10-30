package com.examly.springapp.service;

import com.examly.springapp.model.SystemSettings;
import com.examly.springapp.repository.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemSettingsService {

    private final SystemSettingsRepository settingsRepo;

    public SystemSettings getSettings() {
        // Get the first (and typically only) settings record
        return settingsRepo.findAll().stream()
                .findFirst()
                .orElse(getDefaultSettings());
    }

    public SystemSettings saveSettings(SystemSettings settings) {
        // Always update the existing record or create new one with ID 1
        SystemSettings existing = settingsRepo.findAll().stream().findFirst().orElse(null);
        if (existing != null) {
            settings.setId(existing.getId());
        }
        return settingsRepo.save(settings);
    }

    public SystemSettings getDefaultSettings() {
        return SystemSettings.builder()
                .companyName("EnterpriseCRM")
                .timeZone("UTC-5")
                .dateFormat("MM/DD/YYYY")
                .language("en")
                .maxUsers(50)
                .twoFactorAuth(true)
                .passwordExpiryDays(90)
                .sessionTimeout(30)
                .failedLoginAttempts(5)
                .requireStrongPassword(true)
                .emailNotifications(true)
                .slackNotifications(false)
                .salesAlerts(true)
                .systemAlerts(true)
                .dailyReports(false)
                .autoBackup(true)
                .backupFrequency("daily")
                .dataRetention(365)
                .maxFileSize(10)
                .smtpHost("smtp.company.com")
                .smtpPort(587)
                .smtpUsername("")
                .smtpPassword("")
                .fromEmail("noreply@company.com")
                .emailEnabled(true)
                .build();
    }

    public void resetToDefaults() {
        SystemSettings defaults = getDefaultSettings();
        saveSettings(defaults);
    }
}