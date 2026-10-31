package com.subscriptiq.subscriptiq.dto;

import com.subscriptiq.subscriptiq.enums.QuotationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class QuotationResponse {
    private Long id;
    private String quotationNumber;
    private Long customerId;
    private String customerName;
    private Long planId;
    private String planName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private QuotationStatus status;
    private LocalDate validUntil;
    private String notes;
    private LocalDateTime createdAt;

    public QuotationResponse(Long id, String quotationNumber, Long customerId,
            String customerName, Long planId, String planName, Integer quantity,
            BigDecimal unitPrice, BigDecimal subtotal, QuotationStatus status,
            LocalDate validUntil, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.quotationNumber = quotationNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.planId = planId;
        this.planName = planName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.status = status;
        this.validUntil = validUntil;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getQuotationNumber() { return quotationNumber; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public Long getPlanId() { return planId; }
    public String getPlanName() { return planName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public QuotationStatus getStatus() { return status; }
    public LocalDate getValidUntil() { return validUntil; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
