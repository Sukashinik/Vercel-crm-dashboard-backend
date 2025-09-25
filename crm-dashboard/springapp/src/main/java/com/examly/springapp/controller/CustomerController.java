package com.examly.springapp.controller;

import com.examly.springapp.model.Customer;
import com.examly.springapp.model.Interaction;
import com.examly.springapp.repository.CustomerRepository;
import com.examly.springapp.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InteractionRepository interactionRepository;

    // ✅ GET all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ GET customer by ID
    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id);
    }

    // ✅ POST create a new customer
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    // ✅ PUT update customer profile
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(customerDetails.getName());
            customer.setEmail(customerDetails.getEmail());
            customer.setPhone(customerDetails.getPhone());
            customer.setCompany(customerDetails.getCompany());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // ✅ POST add interaction to customer
    @PostMapping("/{id}/interactions")
    public Interaction addInteraction(@PathVariable Long id, @RequestBody Interaction interaction) {
        return customerRepository.findById(id).map(customer -> {
            interaction.setCustomer(customer);
            return interactionRepository.save(interaction);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
