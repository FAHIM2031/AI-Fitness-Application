package org.fitness.aifitnesswebapplication.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.fitness.aifitnesswebapplication.dto.RegisterRequest;
import org.fitness.aifitnesswebapplication.dto.UserResponse;
import org.fitness.aifitnesswebapplication.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(userService.register(request));
    }
}
