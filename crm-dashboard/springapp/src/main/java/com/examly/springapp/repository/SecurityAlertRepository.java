package com.examly.springapp.repository;

import com.examly.springapp.model.SecurityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityAlertRepository extends JpaRepository<SecurityAlert, Long> {
    List<SecurityAlert> findByStatusOrderByCreatedAtDesc(SecurityAlert.AlertStatus status);
    List<SecurityAlert> findAllByOrderByCreatedAtDesc();
    long countByStatus(SecurityAlert.AlertStatus status);
    long countByStatus(String status);
}