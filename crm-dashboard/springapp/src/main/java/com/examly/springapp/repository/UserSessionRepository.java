package com.examly.springapp.repository;

import com.examly.springapp.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    List<UserSession> findByStatusOrderByLastActiveDesc(String status);
    List<UserSession> findByUserEmailOrderByLastActiveDesc(String userEmail);
    long countByStatus(String status);
}