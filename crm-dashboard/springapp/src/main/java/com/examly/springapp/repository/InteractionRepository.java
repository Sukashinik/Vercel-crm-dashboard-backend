package com.examly.springapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.examly.springapp.model.Interaction;
import com.examly.springapp.model.User; // Ensure User class exists in this package
import com.examly.springapp.model.Customer;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByCustomer(Customer customer);
    List<Interaction> findByUser(User user);
}