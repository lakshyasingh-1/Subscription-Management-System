
package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProductId(Long productId);

    boolean existsByProductIdAndNameIgnoreCase(
            Long productId, String name
    );
}
