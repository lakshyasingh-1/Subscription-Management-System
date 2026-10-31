package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.RenewSubscriptionRequest;
import com.subscriptiq.subscriptiq.dto.SubscriptionResponse;
import com.subscriptiq.subscriptiq.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/from-quotation/{quotationId}")
    public ResponseEntity<?> createFromQuotation(@PathVariable Long quotationId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(subscriptionService.createFromQuotation(quotationId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(Authentication authentication) {
        boolean staffOrAdmin = hasStaffAccess(authentication);

        System.out.println("Logged-in email: " + authentication.getName());
        System.out.println("Staff or Admin: " + staffOrAdmin);

        try {
            List<SubscriptionResponse> result =
                    subscriptionService.getAllForUser(
                            authentication.getName(), staffOrAdmin);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        try {
            return ResponseEntity.ok(subscriptionService.getByIdForUser(
                    id, authentication.getName(), hasStaffAccess(authentication)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Long id, Authentication auth) {
        return execute(() -> subscriptionService.activate(id, auth.getName(), hasStaffAccess(auth)));
    }

    @PutMapping("/{id}/pause")
    public ResponseEntity<?> pause(@PathVariable Long id, Authentication auth) {
        return execute(() -> subscriptionService.pause(id, auth.getName(), hasStaffAccess(auth)));
    }

    @PutMapping("/{id}/resume")
    public ResponseEntity<?> resume(@PathVariable Long id, Authentication auth) {
        return execute(() -> subscriptionService.resume(id, auth.getName(), hasStaffAccess(auth)));
    }

    @PutMapping("/{id}/renew")
    public ResponseEntity<?> renew(@PathVariable Long id,
            @Valid @RequestBody RenewSubscriptionRequest request, Authentication auth) {
        return execute(() -> subscriptionService.renew(id, request.getNextBillingDate(),
                auth.getName(), hasStaffAccess(auth)));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<?> close(@PathVariable Long id, Authentication auth) {
        return execute(() -> subscriptionService.close(id, auth.getName(), hasStaffAccess(auth)));
    }

    private boolean hasStaffAccess(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_STAFF"));
    }

    private ResponseEntity<?> execute(java.util.function.Supplier<SubscriptionResponse> action) {
        try {
            return ResponseEntity.ok(action.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
