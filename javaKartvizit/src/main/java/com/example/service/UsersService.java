package com.example.service;

import java.util.UUID;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.example.dto.AuthResponse;
import com.example.dto.SignupRequest;
import com.example.dto.LoginRequest;
import com.example.model.UsersModel;
import com.example.repository.UsersRepository;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	
	public AuthResponse signup(SignupRequest request) {
        try {
            // Check if username already exists
            if (usersRepository.existsByUsername(request.getUsername())) {
                return new AuthResponse("Username already exists!", false);
            }
            
            // Check if email already exists
            if (usersRepository.existsByEmail(request.getEmail())) {
                return new AuthResponse("Email already exists!", false);
            }
            
            // Create new user
            UsersModel user = new UsersModel();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            
            UsersModel savedUser = usersRepository.save(user);
            
            // Generate token (simple UUID for demo)
            String token = UUID.randomUUID().toString();
            
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
            );
            
            return new AuthResponse("User registered successfully!", true, token, userInfo);
            
        } catch (Exception e) {
            return new AuthResponse("Registration failed: " + e.getMessage(), false);
        }
    }
	
	public AuthResponse login(LoginRequest request) {
        try {
            // Find user by username
            Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
            
            if (userOptional.isEmpty()) {
                return new AuthResponse("User not found!", false);
            }
            
            User user = userOptional.get();
            
            // Check password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse("Invalid password!", false);
            }
            
            // Generate token (simple UUID for demo)
            String token = UUID.randomUUID().toString();
            
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail()
            );
            
            return new AuthResponse("Login successful!", true, token, userInfo);
            
        } catch (Exception e) {
            return new AuthResponse("Login failed: " + e.getMessage(), false);
        }
    }
}


}
