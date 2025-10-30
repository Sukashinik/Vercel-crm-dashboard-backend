package com.examly.springapp.controller;

import com.examly.springapp.model.User;
import com.examly.springapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET all users - ADMIN only (non-paginated for backward compatibility)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET paginated users - ADMIN only
    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaginatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
            
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = userService.getPaginatedUsers(pageable);
        
        Map<String, Object> response = Map.of(
            "users", userPage.getContent(),
            "currentPage", userPage.getNumber(),
            "totalItems", userPage.getTotalElements(),
            "totalPages", userPage.getTotalPages(),
            "pageSize", userPage.getSize(),
            "hasNext", userPage.hasNext(),
            "hasPrevious", userPage.hasPrevious()
        );
        
        return ResponseEntity.ok(response);
    }

    // POST create new user - ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT update existing user - ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User updated = userService.updateUser(id, updatedUser);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    // DELETE user by ID - ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        try {
            // Prevent admin from deleting themselves
            String currentUsername = authentication.getName();
            User userToDelete = userService.findById(id);
            
            if (userToDelete != null && userToDelete.getUsername().equals(currentUsername)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cannot delete your own account"));
            }
            
            boolean isDeleted = userService.deleteUser(id);
            if (isDeleted) {
                return ResponseEntity.ok().body(Map.of("message", "User deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error deleting user: " + e.getMessage()));
        }
    }

    // GET current user profile - accessible to all authenticated users
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUserProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error fetching profile: " + e.getMessage()));
        }
    }

    // PUT update current user profile - accessible to all authenticated users
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateCurrentUserProfile(@RequestBody User updatedUser, Authentication authentication) {
        try {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser != null) {
                // Only allow updating certain fields for security
                currentUser.setName(updatedUser.getName());
                currentUser.setPhone(updatedUser.getPhone());
                currentUser.setAddress(updatedUser.getAddress());
                // Don't allow changing username, email, role, or password through this endpoint
                
                User updated = userService.updateUser(currentUser.getId(), currentUser);
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error updating profile: " + e.getMessage()));
        }
    }

    // GET basic user list for dropdowns - accessible to authenticated users
    @GetMapping("/basic")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBasicUserList() {
        try {
            List<User> users = userService.getAllUsers();
            // Return only basic info for dropdowns
            List<Map<String, Object>> basicUsers = users.stream()
                .map(user -> {
                    Map<String, Object> userMap = new java.util.HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("name", user.getName());
                    userMap.put("username", user.getUsername());
                    return userMap;
                })
                .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(basicUsers);
        } catch (Exception e) {
            Map<String, Object> errorMap = new java.util.HashMap<>();
            errorMap.put("error", "Error fetching users: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }
    }
}
