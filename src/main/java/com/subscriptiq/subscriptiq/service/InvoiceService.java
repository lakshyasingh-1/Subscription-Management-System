package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.entity.Invoice;
import com.subscriptiq.subscriptiq.entity.InvoiceLine;
import com.subscriptiq.subscriptiq.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BillingCalculationService billingCalculationService;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            BillingCalculationService billingCalculationService) {

        this.invoiceRepository = invoiceRepository;
        this.billingCalculationService = billingCalculationService;
    }

    public Invoice generateInvoice(
            Long subscriptionId,
            String customerName,
            String planName,
            Double unitPrice,
            Integer quantity,
            Double discountPercentage,
            Double gstPercentage) {

        LocalDate invoiceDate = LocalDate.now();

        if (invoiceRepository
                .findBySubscriptionIdAndInvoiceDate(
                        subscriptionId,
                        invoiceDate
                )
                .isPresent()) {

            throw new RuntimeException(
                    "Invoice already exists for this subscription today"
            );
        }

        Double baseAmount =
                billingCalculationService.calculateBasePrice(
                        unitPrice,
                        quantity
                );

        Double discountAmount = 0.0;

        if (discountPercentage != null
                && discountPercentage > 0) {

            discountAmount =
                    baseAmount * discountPercentage / 100;
        }

        Double discountedAmount =
                baseAmount - discountAmount;

        Double gstAmount = 0.0;

        if (gstPercentage != null
                && gstPercentage > 0) {

            gstAmount =
                    discountedAmount * gstPercentage / 100;
        }

        Double finalAmount =
                discountedAmount + gstAmount;

        Invoice invoice = new Invoice();

        invoice.setSubscriptionId(subscriptionId);
        invoice.setCustomerName(customerName);

        invoice.setBaseAmount(baseAmount);
        invoice.setDiscountAmount(discountAmount);
        invoice.setGstAmount(gstAmount);
        invoice.setTotalAmount(finalAmount);

        invoice.setInvoiceDate(invoiceDate);
        invoice.setStatus("UNPAID");

        InvoiceLine invoiceLine = new InvoiceLine();

        invoiceLine.setDescription(planName);
        invoiceLine.setQuantity(quantity);
        invoiceLine.setUnitPrice(unitPrice);
        invoiceLine.setAmount(baseAmount);
        invoiceLine.setInvoice(invoice);

        invoice.getInvoiceLines().add(invoiceLine);

        return invoiceRepository.save(invoice);
    }
}