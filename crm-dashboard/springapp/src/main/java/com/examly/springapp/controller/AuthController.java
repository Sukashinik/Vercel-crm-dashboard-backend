package com.examly.springapp.controller;

import com.examly.springapp.model.AuthRequest;
import com.examly.springapp.model.AuthResponse;
import com.examly.springapp.model.User;
import com.examly.springapp.security.AuthDetails;
import com.examly.springapp.security.JwtUtil;
import com.examly.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthDetails authDetails;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login API
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        final UserDetails userDetails = authDetails.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails.getUsername());
        return new AuthResponse(token);
    }

    // Register / Signup API
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            return "Username already taken!";
        }
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }
}
