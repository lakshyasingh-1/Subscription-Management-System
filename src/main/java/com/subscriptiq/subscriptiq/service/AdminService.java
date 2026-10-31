package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.StaffRequest;
import com.subscriptiq.subscriptiq.entity.User;
import com.subscriptiq.subscriptiq.enums.Role;
import com.subscriptiq.subscriptiq.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createStaff(StaffRequest request) {

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

        User staff = new User();

        staff.setFullName(request.getFullName().trim());
        staff.setEmail(email);

        staff.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        staff.setRole(Role.STAFF);

        userRepository.save(staff);

        return "Staff account created successfully!";
    }
}