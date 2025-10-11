package com.examly.springapp.controller;

import com.examly.springapp.model.Interaction;
import com.examly.springapp.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    // Get all interactions (normal)
    @GetMapping
    public List<Interaction> getAllInteractions() {
        return interactionService.getAllInteractions();
    }

    // Get paginated & sorted interactions
    // Example: /api/interactions/paginated?page=0&size=5&sortBy=timestamp&direction=desc
    @GetMapping("/paginated")
    public Page<Interaction> getAllInteractionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return interactionService.getAllInteractionsPaginated(page, size, sortBy, direction);
    }

    // Get single interaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteractionById(@PathVariable Long id) {
        try {
            Interaction interaction = interactionService.getInteractionById(id);
            return ResponseEntity.ok(interaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create new interaction
    @PostMapping
    public ResponseEntity<Interaction> logInteraction(
            @RequestParam Long customerId,
            @RequestParam Long userId,
            @RequestBody Interaction interaction) {
        try {
            Interaction savedInteraction = interactionService.logInteraction(customerId, userId, interaction);
            return new ResponseEntity<>(savedInteraction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete interaction by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInteraction(@PathVariable Long id) {
        try {
            interactionService.deleteInteraction(id);
            return ResponseEntity.ok("Interaction deleted successfully with ID: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Interaction not found with ID: " + id);
        }
    }
}
