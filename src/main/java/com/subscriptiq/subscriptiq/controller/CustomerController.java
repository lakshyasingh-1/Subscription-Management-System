
package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.CustomerRequest;
import com.subscriptiq.subscriptiq.entity.Customer;
import com.subscriptiq.subscriptiq.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create Customer
    @PostMapping
    public ResponseEntity<?> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        try {
            Customer customer = customerService.createCustomer(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(customer);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get All Customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {

        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }

    // Get Customer By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    customerService.getCustomerById(id)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        try {
            Customer customer = customerService.updateCustomer(id, request);

            return ResponseEntity.ok(customer);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

        try {
            customerService.deleteCustomer(id);

            return ResponseEntity.ok("Customer deleted successfully!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
