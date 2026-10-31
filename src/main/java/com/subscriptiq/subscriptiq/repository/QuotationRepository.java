package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.Quotation;
import com.subscriptiq.subscriptiq.enums.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Optional<Quotation> findByQuotationNumber(String quotationNumber);
    List<Quotation> findByCustomerId(Long customerId);
    List<Quotation> findByStatus(QuotationStatus status);
    boolean existsByQuotationNumber(String quotationNumber);
}
