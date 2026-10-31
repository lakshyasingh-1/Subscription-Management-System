package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findBySubscriptionIdAndInvoiceDate(
            Long subscriptionId,
            LocalDate invoiceDate
    );

    List<Invoice> findBySubscriptionId(
            Long subscriptionId
    );

    List<Invoice> findByCustomerName(
            String customerName
    );
}