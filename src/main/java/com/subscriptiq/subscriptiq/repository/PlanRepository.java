
package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByVariantId(Long variantId);

    boolean existsByVariantIdAndNameIgnoreCase(
            Long variantId, String name
    );
}
