
package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.ProductVariantRequest;
import com.subscriptiq.subscriptiq.entity.Product;
import com.subscriptiq.subscriptiq.entity.ProductVariant;
import com.subscriptiq.subscriptiq.repository.ProductRepository;
import com.subscriptiq.subscriptiq.repository.ProductVariantRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(
            ProductVariantRepository variantRepository,
            ProductRepository productRepository) {

        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    public ProductVariant createVariant(ProductVariantRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is inactive");
        }

        String name = request.getName().trim();

        if (variantRepository.existsByProductIdAndNameIgnoreCase(
                product.getId(), name)) {

            throw new IllegalArgumentException(
                    "Variant with this name already exists for this product");
        }

        ProductVariant variant = new ProductVariant();

        variant.setProduct(product);
        variant.setName(name);
        variant.setDescription(request.getDescription());

        return variantRepository.save(variant);
    }

    public List<ProductVariant> getVariantsByProductId(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found");
        }

        return variantRepository.findByProductId(productId);
    }

    public ProductVariant getVariantById(Long id) {

        return variantRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Variant not found"));
    }
}

