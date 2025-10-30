package com.examly.springapp.service;

import com.examly.springapp.model.Customer;
import com.examly.springapp.model.Interaction;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.CustomerRepository;
import com.examly.springapp.repository.InteractionRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    // Get all interactions (no pagination)
    public List<Interaction> getAllInteractions() {
        return interactionRepo.findAll();
    }
    
    // Get all interactions with pagination & sorting
    public Page<Interaction> getAllInteractionsPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return interactionRepo.findAll(pageable);
    }

    // Get interaction by ID
    public Interaction getInteractionById(Long id) {
        return interactionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interaction not found with ID: " + id));
    }

    // Log new interaction
    public Interaction logInteraction(Long customerId, Long userId, Interaction interaction) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        interaction.setCustomer(customer);
        interaction.setUser(user);
        return interactionRepo.save(interaction);
    }
    
    // Update interaction
    public Interaction updateInteraction(Long id, Interaction interactionDetails) {
        Interaction existingInteraction = interactionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Interaction not found with ID: " + id));
        
        // Update fields
        if (interactionDetails.getType() != null) {
            existingInteraction.setType(interactionDetails.getType());
        }
        if (interactionDetails.getNotes() != null) {
            existingInteraction.setNotes(interactionDetails.getNotes());
        }
        if (interactionDetails.getTimestamp() != null) {
            existingInteraction.setTimestamp(interactionDetails.getTimestamp());
        }
        
        return interactionRepo.save(existingInteraction);
    }

    // Delete interaction by ID
    public void deleteInteraction(Long id) {
        if (!interactionRepo.existsById(id)) {
            throw new IllegalArgumentException("Interaction not found with ID: " + id);
        }
        interactionRepo.deleteById(id);
    }

    // Get interactions by customer ID
    public List<Interaction> getInteractionsByCustomerId(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        return interactionRepo.findByCustomer(customer);
    }

    // Get interactions by user ID
    public List<Interaction> getInteractionsByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return interactionRepo.findByUser(user);
    }
}