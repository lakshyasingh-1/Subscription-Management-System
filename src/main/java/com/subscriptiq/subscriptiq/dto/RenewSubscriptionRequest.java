package com.subscriptiq.subscriptiq.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RenewSubscriptionRequest {
    @NotNull
    private LocalDate nextBillingDate;

    public LocalDate getNextBillingDate() { return nextBillingDate; }
    public void setNextBillingDate(LocalDate nextBillingDate) { this.nextBillingDate = nextBillingDate; }
}
