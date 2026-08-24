package com.freelancing.freelancing_platform.service;

import com.freelancing.freelancing_platform.dto.SignupRequest;
import com.freelancing.freelancing_platform.entity.User;
import com.freelancing.freelancing_platform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                   PasswordEncoder passwordEncoder,
                   JwtService jwtService) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}
public String login(String email, String password) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid email or password");
    }

    return jwtService.generateToken(user.getEmail(),user.getRole().name());
}
    public User signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        return userRepository.save(user);
    }
}