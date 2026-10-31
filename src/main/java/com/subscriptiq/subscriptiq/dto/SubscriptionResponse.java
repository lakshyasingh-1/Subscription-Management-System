package com.subscriptiq.subscriptiq.dto;

import com.subscriptiq.subscriptiq.enums.SubscriptionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SubscriptionResponse {
    private Long id;
    private String subscriptionNumber;
    private Long customerId;
    private String customerName;
    private Long planId;
    private String planName;
    private Integer quantity;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextBillingDate;
    private LocalDateTime createdAt;

    public SubscriptionResponse(Long id, String subscriptionNumber, Long customerId,
            String customerName, Long planId, String planName, Integer quantity,
            SubscriptionStatus status, LocalDate startDate, LocalDate endDate,
            LocalDate nextBillingDate, LocalDateTime createdAt) {
        this.id = id;
        this.subscriptionNumber = subscriptionNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.planId = planId;
        this.planName = planName;
        this.quantity = quantity;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextBillingDate = nextBillingDate;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getSubscriptionNumber() { return subscriptionNumber; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public Long getPlanId() { return planId; }
    public String getPlanName() { return planName; }
    public Integer getQuantity() { return quantity; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getNextBillingDate() { return nextBillingDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
