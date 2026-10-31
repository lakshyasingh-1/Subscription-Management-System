package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.entity.Invoice;
import com.subscriptiq.subscriptiq.repository.InvoiceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(
            @PathVariable Long id) {

        return invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found")
                );
    }

    @GetMapping("/subscription/{subscriptionId}")
    public List<Invoice> getInvoicesBySubscription(
            @PathVariable Long subscriptionId) {

        return invoiceRepository
                .findBySubscriptionId(subscriptionId);
    }

    @GetMapping("/customer/{customerName}")
    public List<Invoice> getInvoicesByCustomer(
            @PathVariable String customerName) {

        return invoiceRepository
                .findByCustomerName(customerName);
    }
}