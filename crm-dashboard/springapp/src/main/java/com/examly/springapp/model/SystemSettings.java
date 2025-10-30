package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_settings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SystemSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // General Settings
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "time_zone")
    private String timeZone;
    
    @Column(name = "date_format")
    private String dateFormat;
    
    private String language;
    
    @Column(name = "max_users")
    private Integer maxUsers;

    // Security Settings
    @Column(name = "two_factor_auth")
    private Boolean twoFactorAuth;
    
    @Column(name = "password_expiry_days")
    private Integer passwordExpiryDays;
    
    @Column(name = "session_timeout")
    private Integer sessionTimeout;
    
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;
    
    @Column(name = "require_strong_password")
    private Boolean requireStrongPassword;

    // Notification Settings
    @Column(name = "email_notifications")
    private Boolean emailNotifications;
    
    @Column(name = "slack_notifications")
    private Boolean slackNotifications;
    
    @Column(name = "sales_alerts")
    private Boolean salesAlerts;
    
    @Column(name = "system_alerts")
    private Boolean systemAlerts;
    
    @Column(name = "daily_reports")
    private Boolean dailyReports;

    // Email Settings
    @Column(name = "smtp_host")
    private String smtpHost;
    
    @Column(name = "smtp_port")
    private Integer smtpPort;
    
    @Column(name = "smtp_username")
    private String smtpUsername;
    
    @Column(name = "smtp_password")
    private String smtpPassword;
    
    @Column(name = "from_email")
    private String fromEmail;
    
    @Column(name = "email_enabled")
    private Boolean emailEnabled;

    // Data & Storage Settings
    @Column(name = "auto_backup")
    private Boolean autoBackup;
    
    @Column(name = "backup_frequency")
    private String backupFrequency;
    
    @Column(name = "data_retention")
    private Integer dataRetention;
    
    @Column(name = "max_file_size")
    private Integer maxFileSize;
}