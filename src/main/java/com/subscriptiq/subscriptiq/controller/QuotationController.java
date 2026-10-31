package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.QuotationRequest;
import com.subscriptiq.subscriptiq.dto.QuotationResponse;
import com.subscriptiq.subscriptiq.dto.QuotationStatusRequest;
import com.subscriptiq.subscriptiq.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody QuotationRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(quotationService.createQuotation(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<QuotationResponse>> getAll() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(quotationService.getQuotationById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomer(@PathVariable Long customerId) {
        try {
            return ResponseEntity.ok(quotationService.getByCustomerId(customerId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
            @Valid @RequestBody QuotationStatusRequest request) {
        try {
            return ResponseEntity.ok(
                    quotationService.updateStatus(id, request.getStatus()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
