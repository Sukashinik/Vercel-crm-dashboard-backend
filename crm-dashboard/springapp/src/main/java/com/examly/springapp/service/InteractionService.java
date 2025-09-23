package com.examly.springapp.service;

import com.examly.springapp.model.Customer;
import com.examly.springapp.model.Interaction;
import com.examly.springapp.repository.CustomerRepository;
import com.examly.springapp.repository.InteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final InteractionRepository interactionRepo;
    private final CustomerRepository customerRepo;

    public Interaction logInteraction(Long customerId, Interaction interaction) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        interaction.setCustomer(customer);
        return interactionRepo.save(interaction);
    }
}

