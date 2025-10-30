package com.examly.springapp.service;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.repository.InteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final InteractionRepository interactionRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Page<User> getPaginatedUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public User createUser(User user) {
        // Encode password before saving
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepo.save(user);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }
    
    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
    
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    // Update user by ID (excluding password)
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOpt = userRepo.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            // Don't update password in regular update - keep existing password
            return userRepo.save(existingUser);
        }
        return null;
    }

    // Delete user by ID with cascade handling
    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            // First, set user_id to null in all interactions for this user
            interactionRepo.setUserToNullByUserId(id);
            // Then delete the user
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
