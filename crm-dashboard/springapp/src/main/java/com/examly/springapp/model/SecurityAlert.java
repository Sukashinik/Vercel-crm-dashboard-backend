package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_alerts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SecurityAlert {

    public enum AlertType {
        HIGH, MEDIUM, LOW
    }

    public enum AlertStatus {
        ACTIVE, INVESTIGATING, RESOLVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "source_ip")
    private String sourceIp;

    @Column(name = "affected_user")
    private String affectedUser;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = AlertStatus.ACTIVE;
        }
    }
}