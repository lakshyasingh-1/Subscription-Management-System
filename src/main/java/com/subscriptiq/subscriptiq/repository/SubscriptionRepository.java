package com.subscriptiq.subscriptiq.repository;

import com.subscriptiq.subscriptiq.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    List<Subscription> findByStatus(String status);
}