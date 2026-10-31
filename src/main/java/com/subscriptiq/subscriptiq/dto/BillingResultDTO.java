package com.subscriptiq.subscriptiq.dto;

public class BillingResultDTO {

    private Long billingRunId;
    private int totalSubscriptions;
    private int successfulBills;
    private int failedBills;
    private Double totalAmount;
    private String status;

    public BillingResultDTO() {
    }

    public BillingResultDTO(Long billingRunId,
                            int totalSubscriptions,
                            int successfulBills,
                            int failedBills,
                            Double totalAmount,
                            String status) {
        this.billingRunId = billingRunId;
        this.totalSubscriptions = totalSubscriptions;
        this.successfulBills = successfulBills;
        this.failedBills = failedBills;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getBillingRunId() {
        return billingRunId;
    }

    public void setBillingRunId(Long billingRunId) {
        this.billingRunId = billingRunId;
    }

    public int getTotalSubscriptions() {
        return totalSubscriptions;
    }

    public void setTotalSubscriptions(int totalSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
    }

    public int getSuccessfulBills() {
        return successfulBills;
    }

    public void setSuccessfulBills(int successfulBills) {
        this.successfulBills = successfulBills;
    }

    public int getFailedBills() {
        return failedBills;
    }

    public void setFailedBills(int failedBills) {
        this.failedBills = failedBills;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}