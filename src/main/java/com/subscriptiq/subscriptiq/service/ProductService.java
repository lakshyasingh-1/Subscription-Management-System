
package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.ProductRequest;
import com.subscriptiq.subscriptiq.entity.Product;
import com.subscriptiq.subscriptiq.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequest request) {

        String name = request.getName().trim();

        if (productRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                    "Product with this name already exists"
            );
        }

        Product product = new Product();

        product.setName(name);
        product.setDescription(request.getDescription());

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found")
                );
    }

    public Product updateProduct(Long id, ProductRequest request) {

        Product product = getProductById(id);

        String name = request.getName().trim();

        if (!product.getName().equalsIgnoreCase(name)
                && productRepository.existsByName(name)) {

            throw new IllegalArgumentException(
                    "Another product with this name already exists"
            );
        }

        product.setName(name);
        product.setDescription(request.getDescription());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {

        Product product = getProductById(id);

        productRepository.delete(product);
    }
}
