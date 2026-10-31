package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.Subscription;
import com.subscriptiq.subscriptiq.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCustomerId(Long customerId);
    List<Subscription> findByStatus(SubscriptionStatus status);
    Optional<Subscription> findBySubscriptionNumber(String subscriptionNumber);
    boolean existsByQuotationId(Long quotationId);
}
