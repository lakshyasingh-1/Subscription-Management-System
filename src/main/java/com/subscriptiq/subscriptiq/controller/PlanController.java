
package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.PlanRequest;
import com.subscriptiq.subscriptiq.entity.Plan;
import com.subscriptiq.subscriptiq.service.PlanService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // Create Plan
    @PostMapping
    public ResponseEntity<?> createPlan(
            @Valid @RequestBody PlanRequest request) {

        try {
            Plan plan = planService.createPlan(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(plan);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get Plans by Variant ID
    @GetMapping("/variant/{variantId}")
    public ResponseEntity<?> getPlansByVariantId(
            @PathVariable Long variantId) {

        try {
            List<Plan> plans =
                    planService.getPlansByVariantId(variantId);

            return ResponseEntity.ok(plans);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // Get Plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(planService.getPlanById(id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // Update Plan
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody PlanRequest request) {

        try {
            Plan plan = planService.updatePlan(id, request);

            return ResponseEntity.ok(plan);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Delete Plan
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {

        try {
            planService.deletePlan(id);

            return ResponseEntity.ok("Plan deleted successfully!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
