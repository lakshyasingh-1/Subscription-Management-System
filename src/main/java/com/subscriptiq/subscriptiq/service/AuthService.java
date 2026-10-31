
package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.LoginRequest;
import com.subscriptiq.subscriptiq.dto.RegisterRequest;
import com.subscriptiq.subscriptiq.entity.User;
import com.subscriptiq.subscriptiq.enums.Role;
import com.subscriptiq.subscriptiq.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {

        if (request.getFullName() == null ||
                request.getFullName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null ||
                request.getPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters"
            );
        }

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        User user = new User();

        user.setFullName(request.getFullName().trim());
        user.setEmail(email);

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Public registration always creates CUSTOMER
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        return "Registration successful!";
    }
    public String login(LoginRequest request) {

        // Find user using email
        User user = userRepository.findByEmail(
                request.getEmail().trim().toLowerCase()
        ).orElseThrow(() ->
                new IllegalArgumentException("User not found")
        );

        // Check password
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new IllegalArgumentException("Invalid password");
        }

        return "Login successful!";
    }
}
