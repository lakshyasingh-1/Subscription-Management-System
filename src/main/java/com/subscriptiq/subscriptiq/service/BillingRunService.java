package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.BillingPreviewDTO;
import com.subscriptiq.subscriptiq.dto.BillingResultDTO;
import com.subscriptiq.subscriptiq.dto.BillingRunRequestDTO;
import com.subscriptiq.subscriptiq.entity.BillingRun;
import com.subscriptiq.subscriptiq.entity.BillingRunItem;
import com.subscriptiq.subscriptiq.entity.Invoice;
import com.subscriptiq.subscriptiq.entity.Subscription;
import com.subscriptiq.subscriptiq.repository.BillingRunItemRepository;
import com.subscriptiq.subscriptiq.repository.BillingRunRepository;
import com.subscriptiq.subscriptiq.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillingRunService {

    private final BillingRunRepository billingRunRepository;
    private final BillingRunItemRepository billingRunItemRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceService invoiceService;

    public BillingRunService(
            BillingRunRepository billingRunRepository,
            BillingRunItemRepository billingRunItemRepository,
            SubscriptionRepository subscriptionRepository,
            InvoiceService invoiceService) {

        this.billingRunRepository = billingRunRepository;
        this.billingRunItemRepository = billingRunItemRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceService = invoiceService;
    }

    public List<BillingPreviewDTO> getBillingPreview() {

        List<Subscription> activeSubscriptions =
                subscriptionRepository.findByStatus("ACTIVE");

        List<BillingPreviewDTO> preview =
                new ArrayList<>();

        for (Subscription subscription : activeSubscriptions) {

            double price = subscription.getPrice() != null
                    ? subscription.getPrice()
                    : 0.0;

            int quantity = subscription.getQuantity() != null
                    ? subscription.getQuantity()
                    : 1;

            double discount = subscription.getDiscountPercentage() != null
                    ? subscription.getDiscountPercentage()
                    : 0.0;

            double gst = subscription.getGstPercentage() != null
                    ? subscription.getGstPercentage()
                    : 0.0;

            double baseAmount = price * quantity;

            double discountAmount =
                    baseAmount * discount / 100;

            double discountedAmount =
                    baseAmount - discountAmount;

            double gstAmount =
                    discountedAmount * gst / 100;

            double finalAmount =
                    discountedAmount + gstAmount;

            BillingPreviewDTO dto =
                    new BillingPreviewDTO(
                            subscription.getId(),
                            subscription.getCustomerName(),
                            subscription.getPlanName(),
                            finalAmount,
                            LocalDate.now().toString(),
                            "READY"
                    );

            preview.add(dto);
        }

        return preview;
    }

    public BillingResultDTO runBilling(
            BillingRunRequestDTO request) {

        BillingRun billingRun = new BillingRun();

        billingRun.setRunDate(LocalDateTime.now());
        billingRun.setStatus("STARTED");

        BillingRun savedRun =
                billingRunRepository.save(billingRun);

        List<Subscription> activeSubscriptions =
                subscriptionRepository.findByStatus("ACTIVE");

        int totalSubscriptions =
                activeSubscriptions.size();

        int successfulBills = 0;
        int failedBills = 0;
        double totalAmount = 0.0;

        for (Subscription subscription :
                activeSubscriptions) {

            BillingRunItem item =
                    new BillingRunItem();

            item.setSubscriptionId(
                    subscription.getId()
            );

            item.setCustomerName(
                    subscription.getCustomerName()
            );

            item.setPlanName(
                    subscription.getPlanName()
            );

            item.setBillingRun(savedRun);

            try {

                if (request.isDryRun()) {

                    double price =
                            subscription.getPrice() != null
                                    ? subscription.getPrice()
                                    : 0.0;

                    int quantity =
                            subscription.getQuantity() != null
                                    ? subscription.getQuantity()
                                    : 1;

                    double discount =
                            subscription.getDiscountPercentage() != null
                                    ? subscription.getDiscountPercentage()
                                    : 0.0;

                    double gst =
                            subscription.getGstPercentage() != null
                                    ? subscription.getGstPercentage()
                                    : 0.0;

                    double baseAmount =
                            price * quantity;

                    double discountAmount =
                            baseAmount * discount / 100;

                    double discountedAmount =
                            baseAmount - discountAmount;

                    double gstAmount =
                            discountedAmount * gst / 100;

                    double finalAmount =
                            discountedAmount + gstAmount;

                    item.setAmount(finalAmount);
                    item.setStatus("READY");

                    successfulBills++;
                    totalAmount += finalAmount;

                } else {

                    Invoice invoice =
                            invoiceService.generateInvoice(
                                    subscription.getId(),
                                    subscription.getCustomerName(),
                                    subscription.getPlanName(),
                                    subscription.getPrice(),
                                    subscription.getQuantity(),
                                    subscription.getDiscountPercentage(),
                                    subscription.getGstPercentage()
                            );

                    item.setAmount(
                            invoice.getTotalAmount()
                    );

                    item.setStatus("SUCCESS");

                    successfulBills++;

                    totalAmount +=
                            invoice.getTotalAmount();
                }

            } catch (Exception e) {

                item.setAmount(0.0);
                item.setStatus("FAILED");

                failedBills++;
            }

            billingRunItemRepository.save(item);
        }

        savedRun.setTotalSubscriptions(
                totalSubscriptions
        );

        savedRun.setSuccessfulBills(
                successfulBills
        );

        savedRun.setFailedBills(
                failedBills
        );

        savedRun.setTotalAmount(
                totalAmount
        );

        if (request.isDryRun()) {

            savedRun.setStatus(
                    "DRY_RUN_COMPLETED"
            );

        } else if (failedBills == 0) {

            savedRun.setStatus(
                    "COMPLETED"
            );

        } else {

            savedRun.setStatus(
                    "COMPLETED_WITH_ERRORS"
            );
        }

        BillingRun finalRun =
                billingRunRepository.save(
                        savedRun
                );

        return new BillingResultDTO(
                finalRun.getId(),
                finalRun.getTotalSubscriptions(),
                finalRun.getSuccessfulBills(),
                finalRun.getFailedBills(),
                finalRun.getTotalAmount(),
                finalRun.getStatus()
        );
    }

    public BillingResultDTO getBillingResult(
            Long billingRunId) {

        BillingRun billingRun =
                billingRunRepository
                        .findById(billingRunId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Billing run not found"
                                )
                        );

        return new BillingResultDTO(
                billingRun.getId(),
                billingRun.getTotalSubscriptions(),
                billingRun.getSuccessfulBills(),
                billingRun.getFailedBills(),
                billingRun.getTotalAmount(),
                billingRun.getStatus()
        );
    }
}