package com.examly.springapp.controller;

import com.examly.springapp.model.Interaction;
import com.examly.springapp.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Added for 201 Created
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;
    
    // GET /api/interactions
    @GetMapping
    public List<Interaction> getAllInteractions() {
        return interactionService.getAllInteractions();
    }

    // GET /api/interactions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteractionById(@PathVariable Long id) {
        try {
            Interaction interaction = interactionService.getInteractionById(id);
            return ResponseEntity.ok(interaction);
        } catch (IllegalArgumentException e) {
            // Assuming IllegalArgumentException is thrown by the service for 'not found'
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Interaction> logInteraction(
            @RequestParam Long customerId,
            @RequestParam Long userId,
            @RequestBody Interaction interaction) {
        try {
            Interaction savedInteraction = interactionService.logInteraction(customerId, userId, interaction);
            // Use status 201 Created for resource creation
            return new ResponseEntity<>(savedInteraction, HttpStatus.CREATED); 
        } catch (IllegalArgumentException e) {
            // Handle cases where customerId or userId are not found
            // Returning 400 Bad Request since the input (IDs) is invalid
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}