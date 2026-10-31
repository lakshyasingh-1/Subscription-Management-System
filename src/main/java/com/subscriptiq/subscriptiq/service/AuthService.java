
package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.LoginRequest;
import com.subscriptiq.subscriptiq.dto.RegisterRequest;
import com.subscriptiq.subscriptiq.entity.User;
import com.subscriptiq.subscriptiq.enums.Role;
import com.subscriptiq.subscriptiq.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Register a new customer
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

    // Authenticate user and return authentication details
    public Authentication login(LoginRequest request) {

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        String email = request.getEmail().trim().toLowerCase();
        User admin = userRepository.findByEmail(email).orElse(null);

        if (admin != null) {
            System.out.println("User found: " + admin.getEmail());
            System.out.println("Password matches: " +
                    passwordEncoder.matches(
                            request.getPassword(),
                            admin.getPassword()
                    ));
        }
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }
    }
}
