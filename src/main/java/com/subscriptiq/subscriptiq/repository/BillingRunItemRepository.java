package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.BillingRunItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRunItemRepository
        extends JpaRepository<BillingRunItem, Long> {
}