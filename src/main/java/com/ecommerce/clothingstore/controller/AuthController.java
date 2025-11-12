package com.ecommerce.clothingstore.controller;

import com.ecommerce.clothingstore.entity.User;
import com.ecommerce.clothingstore.payload.ApiResponse;
import com.ecommerce.clothingstore.repository.UserRepository;
import com.ecommerce.clothingstore.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.ROLE_USER);
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", null));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Map<String, String> credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.get("email"),
                        credentials.get("password")
                )
        );

        String token = jwtUtil.generateToken(credentials.get("email"));
        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        return ResponseEntity.ok(new ApiResponse(true, "Login successful", data));
    }
}
