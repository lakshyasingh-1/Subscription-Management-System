
package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.ProductVariantRequest;
import com.subscriptiq.subscriptiq.entity.ProductVariant;
import com.subscriptiq.subscriptiq.service.ProductVariantService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantController {

    private final ProductVariantService variantService;

    public ProductVariantController(ProductVariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping
    public ResponseEntity<?> createVariant(
            @Valid @RequestBody ProductVariantRequest request) {

        try {
            ProductVariant variant = variantService.createVariant(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(variant);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getVariantsByProductId(
            @PathVariable Long productId) {

        try {
            List<ProductVariant> variants =
                    variantService.getVariantsByProductId(productId);

            return ResponseEntity.ok(variants);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVariantById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(variantService.getVariantById(id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
