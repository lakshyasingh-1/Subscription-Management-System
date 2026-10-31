package com.subscriptiq.subscriptiq.dto;

public class BillingPreviewDTO {

    private Long subscriptionId;
    private String customerName;
    private String planName;
    private Double amount;
    private String billingDate;
    private String status;

    public BillingPreviewDTO() {
    }

    public BillingPreviewDTO(Long subscriptionId,
                             String customerName,
                             String planName,
                             Double amount,
                             String billingDate,
                             String status) {
        this.subscriptionId = subscriptionId;
        this.customerName = customerName;
        this.planName = planName;
        this.amount = amount;
        this.billingDate = billingDate;
        this.status = status;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}