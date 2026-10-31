package com.subscriptiq.subscriptiq.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing_runs")
public class BillingRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime runDate;

    private int totalSubscriptions;

    private int successfulBills;

    private int failedBills;

    private Double totalAmount;

    private String status;

    public BillingRun() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRunDate() {
        return runDate;
    }

    public void setRunDate(LocalDateTime runDate) {
        this.runDate = runDate;
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