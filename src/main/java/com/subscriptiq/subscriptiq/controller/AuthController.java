
package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.LoginRequest;
import com.subscriptiq.subscriptiq.dto.RegisterRequest;
import com.subscriptiq.subscriptiq.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
            AuthService authService,
            SecurityContextRepository securityContextRepository) {

        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
    }

    // Registration API
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        try {
            String message = authService.register(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(message);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    @GetMapping("/csrf")
    public ResponseEntity<String> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken.getToken());
    }
    // Login API with session creation
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {
            Authentication authentication = authService.login(request);

            // Create a new HTTP session
            httpRequest.getSession(true);

            // Change session ID for security
            httpRequest.changeSessionId();

            // Create authenticated security context
            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

            // Save authentication inside HTTP session
            securityContextRepository.saveContext(
                    context,
                    httpRequest,
                    httpResponse
            );

            return ResponseEntity.ok("Login successful!");

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    // Logout API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request) {

        // Clear current authentication
        SecurityContextHolder.clearContext();

        // Invalidate HTTP session
        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logout successful!");
    }
}
