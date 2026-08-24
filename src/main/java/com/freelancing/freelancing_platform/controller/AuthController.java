package com.freelancing.freelancing_platform.controller;

import com.freelancing.freelancing_platform.dto.LoginRequest;
import com.freelancing.freelancing_platform.dto.SignupRequest;
import com.freelancing.freelancing_platform.entity.User;
import com.freelancing.freelancing_platform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
public ResponseEntity<String> login(
        @RequestBody LoginRequest request) {

    String token = userService.login(
            request.getEmail(),
            request.getPassword()
    );

    return ResponseEntity.ok(token);
}

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid SignupRequest request) {

        User user = userService.signup(request);

        return ResponseEntity.ok(user);
    }
}