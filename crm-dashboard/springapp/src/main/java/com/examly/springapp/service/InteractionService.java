package com.examly.springapp.service;

import com.examly.springapp.model.Customer;
import com.examly.springapp.model.Interaction;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.CustomerRepository;
import com.examly.springapp.repository.InteractionRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository interactionRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private UserRepository userRepo;

    // ✅ Get all interactions
    public List<Interaction> getAllInteractions() {
        return interactionRepo.findAll();
    }

    // ✅ Get interaction by ID
    public Interaction getInteractionById(Long id) {
        return interactionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interaction not found with ID: " + id));
    }

    // ✅ Log new interaction
    public Interaction logInteraction(Long customerId, Long userId, Interaction interaction) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        interaction.setCustomer(customer);
        interaction.setUser(user);
        return interactionRepo.save(interaction);
    }
}
