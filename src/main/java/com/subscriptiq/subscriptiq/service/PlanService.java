
package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.PlanRequest;
import com.subscriptiq.subscriptiq.entity.Plan;
import com.subscriptiq.subscriptiq.entity.ProductVariant;
import com.subscriptiq.subscriptiq.repository.PlanRepository;
import com.subscriptiq.subscriptiq.repository.ProductVariantRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final ProductVariantRepository variantRepository;

    public PlanService(
            PlanRepository planRepository,
            ProductVariantRepository variantRepository) {

        this.planRepository = planRepository;
        this.variantRepository = variantRepository;
    }
    public Plan updatePlan(Long id, PlanRequest request) {

        Plan plan = getPlanById(id);

        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Variant not found"));

        if (!variant.isActive()) {
            throw new IllegalArgumentException("Variant is inactive");
        }

        String name = request.getName().trim();
        String billingCycle = request.getBillingCycle().trim().toUpperCase();

        if (!List.of("MONTHLY", "QUARTERLY", "YEARLY").contains(billingCycle)) {
            throw new IllegalArgumentException("Invalid billing cycle");
        }

        if (!plan.getVariant().getId().equals(variant.getId())
                || !plan.getName().equalsIgnoreCase(name)) {

            if (planRepository.existsByVariantIdAndNameIgnoreCase(variant.getId(), name)) {
                throw new IllegalArgumentException("Plan with this name already exists");
            }
        }

        plan.setVariant(variant);
        plan.setName(name);
        plan.setPrice(request.getPrice());
        plan.setBillingCycle(billingCycle);

        return planRepository.save(plan);
    }
    public void deletePlan(Long id) {
        Plan plan = getPlanById(id);
        planRepository.delete(plan);
    }
    public Plan createPlan(PlanRequest request) {

        ProductVariant variant = variantRepository
                .findById(request.getVariantId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Variant not found"));

        if (!variant.isActive()) {
            throw new IllegalArgumentException("Variant is inactive");
        }

        String name = request.getName().trim();
        String billingCycle = request.getBillingCycle()
                .trim().toUpperCase();

        if (!List.of("MONTHLY", "QUARTERLY", "YEARLY")
                .contains(billingCycle)) {
            throw new IllegalArgumentException(
                    "Billing cycle must be MONTHLY, QUARTERLY or YEARLY");
        }

        if (planRepository.existsByVariantIdAndNameIgnoreCase(
                variant.getId(), name)) {
            throw new IllegalArgumentException(
                    "Plan with this name already exists for this variant");
        }

        Plan plan = new Plan();

        plan.setVariant(variant);
        plan.setName(name);
        plan.setPrice(request.getPrice());
        plan.setBillingCycle(billingCycle);

        return planRepository.save(plan);
    }

    public List<Plan> getPlansByVariantId(Long variantId) {

        if (!variantRepository.existsById(variantId)) {
            throw new IllegalArgumentException("Variant not found");
        }

        return planRepository.findByVariantId(variantId);
    }

    public Plan getPlanById(Long id) {

        return planRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Plan not found"));
    }
}
