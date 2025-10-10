package com.demoapp.demoapp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.entities.UserEntity;
import com.demoapp.demoapp.mappers.UserMapper;
import com.demoapp.demoapp.models.UserResponse;
import com.demoapp.demoapp.repositories.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class HelloController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public HelloController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/hello")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = Map.of("message", "Hello, World!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            List<UserResponse> response = users.stream()
                    .map(userMapper::toUserResponse)
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
