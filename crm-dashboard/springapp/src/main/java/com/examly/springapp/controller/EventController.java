package com.examly.springapp.controller;

import com.examly.springapp.model.Event;
import com.examly.springapp.model.EventType;
import com.examly.springapp.model.EventStatus;
import com.examly.springapp.model.User;
import com.examly.springapp.model.Customer;
import com.examly.springapp.service.EventService;
import com.examly.springapp.service.UserService;
import com.examly.springapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"http://localhost:3000"})
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getEventById(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Event createEvent(@RequestBody JsonNode eventData) {
        try {
            System.out.println("Creating event with data: " + eventData.toString());
            
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            System.out.println("Authenticated user: " + username);
            
            // Try to find user by username first, then by email
            User currentUser = userService.findByUsername(username);
            if (currentUser == null) {
                currentUser = userService.findByEmail(username);
            }
            
            if (currentUser == null) {
                System.err.println("User not found: " + username);
                throw new RuntimeException("User not found: " + username);
            }
            
            System.out.println("Found user: " + currentUser.getName() + " with role: " + currentUser.getRole());
            
            // Create new event
            Event event = new Event();
            event.setTitle(eventData.get("title").asText());
            event.setType(EventType.valueOf(eventData.get("type").asText()));
            event.setDescription(eventData.has("description") ? eventData.get("description").asText() : null);
            event.setLocation(eventData.has("location") ? eventData.get("location").asText() : null);
            event.setDuration(eventData.get("duration").asInt());
            event.setStatus(EventStatus.valueOf(eventData.get("status").asText()));
            
            // Parse date
            String dateStr = eventData.get("date").asText();
            LocalDateTime eventDate = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
            event.setDate(eventDate);
            
            // Set the user for the event
            event.setUser(currentUser);
            
            // Handle customer if provided
            if (eventData.has("customerId") && !eventData.get("customerId").isNull()) {
                Long customerId = eventData.get("customerId").asLong();
                Customer customer = customerService.findById(customerId);
                if (customer != null) {
                    event.setCustomer(customer);
                }
            }
            
            return eventService.saveEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Error creating event: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            Event existingEvent = eventService.getEventById(id);
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setType(eventDetails.getType());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setDate(eventDetails.getDate());
            existingEvent.setDuration(eventDetails.getDuration());
            existingEvent.setStatus(eventDetails.getStatus());
            existingEvent.setLocation(eventDetails.getLocation());
            return ResponseEntity.ok(eventService.saveEvent(existingEvent));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}