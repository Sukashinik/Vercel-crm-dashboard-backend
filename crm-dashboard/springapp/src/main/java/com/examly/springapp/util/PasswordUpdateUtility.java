package com.examly.springapp.util;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUpdateUtility implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Updating all user passwords to BCrypt...");

        for (User user : userRepository.findAll()) {
            String rawPassword = user.getPassword();

            // Skip if already BCrypt
            if (!rawPassword.startsWith("$2a$") && !rawPassword.startsWith("$2b$") && !rawPassword.startsWith("$2y$")) {
                String encodedPassword = passwordEncoder.encode(rawPassword);
                user.setPassword(encodedPassword);
                userRepository.save(user);
                System.out.println("Updated password for user: " + user.getUsername());
            }
        }

        System.out.println("Password update completed!");
    }
}
