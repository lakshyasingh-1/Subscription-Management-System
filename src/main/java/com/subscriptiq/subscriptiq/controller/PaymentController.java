package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.PaymentRequestDTO;
import com.subscriptiq.subscriptiq.dto.PaymentResponseDTO;
import com.subscriptiq.subscriptiq.entity.Payment;
import com.subscriptiq.subscriptiq.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponseDTO recordPayment(
            @RequestBody PaymentRequestDTO request) {

        Payment payment = paymentService.recordPayment(
                request.getInvoiceId(),
                request.getAmount(),
                request.getPaymentMethod()
        );

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getInvoice().getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus()
        );
    }
}