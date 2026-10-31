package com.subscriptiq.subscriptiq.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class QuotationRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private Long planId;

    @NotNull
    @Min(1)
    @Max(100000)
    private Integer quantity;

    @NotNull
    private LocalDate validUntil;

    @Size(max = 500)
    private String notes;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
