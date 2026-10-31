package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {
}