package com.examly.springapp.controller;

import com.examly.springapp.model.Interaction;
import com.examly.springapp.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    // Get all interactions (normal)
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Interaction>> getAllInteractions() {
        try {
            List<Interaction> interactions = interactionService.getAllInteractions();
            return ResponseEntity.ok(interactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get paginated & sorted interactions
    @GetMapping("/paginated")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Interaction>> getAllInteractionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Page<Interaction> interactions = interactionService.getAllInteractionsPaginated(page, size, sortBy, direction);
            return ResponseEntity.ok(interactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get single interaction by ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Interaction> getInteractionById(@PathVariable Long id) {
        try {
            Interaction interaction = interactionService.getInteractionById(id);
            return ResponseEntity.ok(interaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new interaction
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Interaction> logInteraction(
            @RequestParam Long customerId,
            @RequestParam Long userId,
            @RequestBody Interaction interaction) {
        try {
            Interaction savedInteraction = interactionService.logInteraction(customerId, userId, interaction);
            return new ResponseEntity<>(savedInteraction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update interaction
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Interaction> updateInteraction(
            @PathVariable Long id,
            @RequestBody Interaction interactionDetails) {
        try {
            Interaction updatedInteraction = interactionService.updateInteraction(id, interactionDetails);
            return ResponseEntity.ok(updatedInteraction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete interaction by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteInteraction(@PathVariable Long id) {
        try {
            interactionService.deleteInteraction(id);
            return ResponseEntity.ok("Interaction deleted successfully with ID: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Interaction not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting interaction: " + e.getMessage());
        }
    }

    // Get interactions by customer ID
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Interaction>> getInteractionsByCustomerId(@PathVariable Long customerId) {
        try {
            List<Interaction> interactions = interactionService.getInteractionsByCustomerId(customerId);
            return ResponseEntity.ok(interactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get interactions by user ID
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Interaction>> getInteractionsByUserId(@PathVariable Long userId) {
        try {
            List<Interaction> interactions = interactionService.getInteractionsByUserId(userId);
            return ResponseEntity.ok(interactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}