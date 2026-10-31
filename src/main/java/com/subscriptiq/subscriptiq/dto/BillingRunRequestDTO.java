package com.subscriptiq.subscriptiq.dto;

public class BillingRunRequestDTO {

    private String billingDate;
    private boolean dryRun;

    public BillingRunRequestDTO() {
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }
}