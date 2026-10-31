package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.StaffRequest;
import com.subscriptiq.subscriptiq.service.AdminService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/test")
    public ResponseEntity<String> testAdmin(
            org.springframework.security.core.Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        return ResponseEntity.ok(
                "Authenticated user: " + authentication.getName()
                        + " | Roles: " + authentication.getAuthorities()
        );
    }
    @PostMapping("/staff")
    public ResponseEntity<String> createStaff(
            @RequestBody StaffRequest request) {

        try {

            String message = adminService.createStaff(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(message);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    @GetMapping("/me")
    public ResponseEntity<String> currentUser(Authentication authentication) {

        return ResponseEntity.ok(
                "User: " + authentication.getName()
                        + " | Roles: " + authentication.getAuthorities()
        );
    }
}