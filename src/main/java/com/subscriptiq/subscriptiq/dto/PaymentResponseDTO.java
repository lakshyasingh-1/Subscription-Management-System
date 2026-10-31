package com.subscriptiq.subscriptiq.dto;

public class PaymentResponseDTO {

    private Long paymentId;
    private Long invoiceId;
    private Double amount;
    private String paymentMethod;
    private String status;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(
            Long paymentId,
            Long invoiceId,
            Double amount,
            String paymentMethod,
            String status) {

        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}