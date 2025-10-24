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
import com.demoapp.demoapp.services.HelloService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class HelloController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HelloService helloService;

    /**
     * Handles GET requests to the "/hello" endpoint.
     * <p>
     * This endpoint is secured and requires the user to be authenticated.
     * When accessed, it returns a JSON response containing a greeting message
     * obtained from the {@link HelloService}.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a {@link Map} with a single key "message"
     *         whose value is the greeting string from {@link HelloService#sayHello()}.
     */
    @GetMapping("/hello")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = Map.of("message", helloService.sayHello());
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @return a {@link ResponseEntity} containing a {@link List} of {@link UserResponse}"
     */
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
