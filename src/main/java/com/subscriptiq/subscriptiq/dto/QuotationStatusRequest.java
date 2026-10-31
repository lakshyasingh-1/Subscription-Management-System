package com.subscriptiq.subscriptiq.dto;

import com.subscriptiq.subscriptiq.enums.QuotationStatus;
import jakarta.validation.constraints.NotNull;

public class QuotationStatusRequest {

    @NotNull
    private QuotationStatus status;

    public QuotationStatus getStatus() { return status; }
    public void setStatus(QuotationStatus status) { this.status = status; }
}
