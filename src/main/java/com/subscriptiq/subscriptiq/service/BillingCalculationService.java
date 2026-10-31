package com.subscriptiq.subscriptiq.service;

import org.springframework.stereotype.Service;

@Service
public class BillingCalculationService {

    // Calculate product/subscription price
    public Double calculateBasePrice(Double unitPrice, Integer quantity) {

        if (unitPrice == null || quantity == null) {
            return 0.0;
        }

        return unitPrice * quantity;
    }

    // Apply discount
    public Double applyDiscount(Double baseAmount, Double discountPercentage) {

        if (baseAmount == null) {
            return 0.0;
        }

        if (discountPercentage == null || discountPercentage <= 0) {
            return baseAmount;
        }

        Double discountAmount =
                baseAmount * discountPercentage / 100;

        return baseAmount - discountAmount;
    }

    // Calculate GST
    public Double calculateGST(Double amount, Double gstPercentage) {

        if (amount == null) {
            return 0.0;
        }

        if (gstPercentage == null || gstPercentage <= 0) {
            return 0.0;
        }

        return amount * gstPercentage / 100;
    }

    // Calculate final invoice amount
    public Double calculateFinalAmount(
            Double baseAmount,
            Double discountPercentage,
            Double gstPercentage) {

        Double discountedAmount =
                applyDiscount(baseAmount, discountPercentage);

        Double gstAmount =
                calculateGST(discountedAmount, gstPercentage);

        return discountedAmount + gstAmount;
    }
}