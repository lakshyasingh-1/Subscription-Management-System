
package com.subscriptiq.subscriptiq.config;

import com.subscriptiq.subscriptiq.entity.User;
import com.subscriptiq.subscriptiq.enums.Role;
import com.subscriptiq.subscriptiq.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.name:}")
    private String adminName;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    public AdminDataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Do not create an admin if credentials are not configured
        if (adminName.isBlank() ||
                adminEmail.isBlank() ||
                adminPassword.isBlank()) {

            System.out.println("Admin setup skipped: credentials not configured.");
            return;
        }

        String email = adminEmail.trim().toLowerCase();

        // Avoid creating duplicate admin accounts
        if (userRepository.existsByEmail(email)) {
            System.out.println("Initial admin account already exists.");
            return;
        }

        if (adminPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "Initial admin password must contain at least 8 characters"
            );
        }

        User admin = new User();

        admin.setFullName(adminName.trim());
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println("Initial admin account created successfully.");
    }
}
