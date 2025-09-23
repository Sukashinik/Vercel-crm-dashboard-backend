package com.examly.springapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {}

