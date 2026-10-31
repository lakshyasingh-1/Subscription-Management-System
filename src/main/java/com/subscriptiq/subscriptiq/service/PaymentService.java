package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.entity.Invoice;
import com.subscriptiq.subscriptiq.entity.Payment;
import com.subscriptiq.subscriptiq.repository.InvoiceRepository;
import com.subscriptiq.subscriptiq.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository) {

        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public Payment recordPayment(
            Long invoiceId,
            Double amount,
            String paymentMethod) {

        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found")
                );

        if (amount == null || amount <= 0) {
            throw new RuntimeException(
                    "Payment amount must be greater than zero"
            );
        }

        List<Payment> previousPayments =
                paymentRepository.findByInvoiceId(invoiceId);

        Double alreadyPaid = previousPayments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        Double remainingAmount =
                invoice.getTotalAmount() - alreadyPaid;

        if (amount > remainingAmount) {
            throw new RuntimeException(
                    "Payment exceeds remaining invoice amount"
            );
        }

        Payment payment = new Payment();

        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("SUCCESS");
        payment.setInvoice(invoice);

        Payment savedPayment =
                paymentRepository.save(payment);

        Double totalPaid =
                alreadyPaid + amount;

        if (totalPaid >= invoice.getTotalAmount()) {

            invoice.setStatus("PAID");

        } else {

            invoice.setStatus("PARTIALLY_PAID");
        }

        invoiceRepository.save(invoice);

        return savedPayment;
    }
}