package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.SubscriptionResponse;
import com.subscriptiq.subscriptiq.entity.Customer;
import com.subscriptiq.subscriptiq.entity.Plan;
import com.subscriptiq.subscriptiq.entity.Quotation;
import com.subscriptiq.subscriptiq.entity.Subscription;
import com.subscriptiq.subscriptiq.enums.QuotationStatus;
import com.subscriptiq.subscriptiq.enums.SubscriptionStatus;
import com.subscriptiq.subscriptiq.repository.CustomerRepository;
import com.subscriptiq.subscriptiq.repository.QuotationRepository;
import com.subscriptiq.subscriptiq.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
            QuotationRepository quotationRepository, CustomerRepository customerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.quotationRepository = quotationRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public SubscriptionResponse createFromQuotation(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new IllegalArgumentException("Quotation not found"));

        if (quotation.getStatus() != QuotationStatus.ACCEPTED) {
            throw new IllegalArgumentException("Only accepted quotations can become subscriptions");
        }
        if (quotation.getValidUntil() != null
                && quotation.getValidUntil().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Quotation has expired");
        }
        if (subscriptionRepository.existsByQuotationId(quotationId)) {
            throw new IllegalArgumentException("Subscription already exists for this quotation");
        }

        Plan plan = quotation.getPlan();
        LocalDate start = LocalDate.now();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionNumber("SUB-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        subscription.setCustomer(quotation.getCustomer());
        subscription.setPlan(plan);
        subscription.setQuotation(quotation);
        subscription.setQuantity(quotation.getQuantity());
        subscription.setStatus(SubscriptionStatus.CONFIRMED);
        subscription.setStartDate(start);
        subscription.setNextBillingDate(calculateNextBillingDate(start, plan.getBillingCycle()));

        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getAllForUser(String email, boolean staffOrAdmin) {
        if (staffOrAdmin) {
            return subscriptionRepository.findAll().stream().map(this::toResponse).toList();
        }
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No customer profile is linked to this login email"));
        return subscriptionRepository.findByCustomerId(customer.getId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getByIdForUser(Long id, String email, boolean staffOrAdmin) {
        Subscription subscription = findSubscription(id);
        checkOwnership(subscription, email, staffOrAdmin);
        return toResponse(subscription);
    }

    @Transactional
    public SubscriptionResponse activate(Long id, String email, boolean staffOrAdmin) {
        Subscription s = findAndCheck(id, email, staffOrAdmin);
        if (s.getStatus() != SubscriptionStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed subscriptions can be activated");
        }
        s.setStatus(SubscriptionStatus.ACTIVE);
        return toResponse(subscriptionRepository.save(s));
    }

    @Transactional
    public SubscriptionResponse pause(Long id, String email, boolean staffOrAdmin) {
        Subscription s = findAndCheck(id, email, staffOrAdmin);
        if (s.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active subscriptions can be paused");
        }
        s.setStatus(SubscriptionStatus.PAUSED);
        return toResponse(subscriptionRepository.save(s));
    }

    @Transactional
    public SubscriptionResponse resume(Long id, String email, boolean staffOrAdmin) {
        Subscription s = findAndCheck(id, email, staffOrAdmin);
        if (s.getStatus() != SubscriptionStatus.PAUSED) {
            throw new IllegalArgumentException("Only paused subscriptions can be resumed");
        }
        s.setStatus(SubscriptionStatus.ACTIVE);
        return toResponse(subscriptionRepository.save(s));
    }

    @Transactional
    public SubscriptionResponse renew(Long id, LocalDate nextBillingDate,
            String email, boolean staffOrAdmin) {
        if (!staffOrAdmin) {
            throw new IllegalArgumentException("Only Admin or Staff can renew subscriptions");
        }
        Subscription s = findAndCheck(id, email, true);
        if (s.getStatus() != SubscriptionStatus.ACTIVE
                && s.getStatus() != SubscriptionStatus.EXPIRED) {
            throw new IllegalArgumentException("Only active or expired subscriptions can be renewed");
        }
        if (!nextBillingDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Next billing date must be in the future");
        }
        s.setNextBillingDate(nextBillingDate);
        s.setEndDate(null);
        s.setStatus(SubscriptionStatus.ACTIVE);
        return toResponse(subscriptionRepository.save(s));
    }

    @Transactional
    public SubscriptionResponse close(Long id, String email, boolean staffOrAdmin) {
        Subscription s = findAndCheck(id, email, staffOrAdmin);
        if (s.getStatus() == SubscriptionStatus.CLOSED) {
            throw new IllegalArgumentException("Subscription is already closed");
        }
        s.setStatus(SubscriptionStatus.CLOSED);
        s.setEndDate(LocalDate.now());
        return toResponse(subscriptionRepository.save(s));
    }

    private Subscription findAndCheck(Long id, String email, boolean staffOrAdmin) {
        Subscription s = findSubscription(id);
        checkOwnership(s, email, staffOrAdmin);
        return s;
    }

    private Subscription findSubscription(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
    }

    private void checkOwnership(Subscription s, String email, boolean staffOrAdmin) {
        if (!staffOrAdmin && !s.getCustomer().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("You are not allowed to access this subscription");
        }
    }

    private LocalDate calculateNextBillingDate(LocalDate start, String cycle) {
        return switch (cycle.toUpperCase()) {
            case "MONTHLY" -> start.plusMonths(1);
            case "QUARTERLY" -> start.plusMonths(3);
            case "YEARLY" -> start.plusYears(1);
            default -> throw new IllegalArgumentException("Unsupported billing cycle");
        };
    }

    private SubscriptionResponse toResponse(Subscription s) {
        return new SubscriptionResponse(s.getId(), s.getSubscriptionNumber(),
                s.getCustomer().getId(), s.getCustomer().getFullName(),
                s.getPlan().getId(), s.getPlan().getName(), s.getQuantity(),
                s.getStatus(), s.getStartDate(), s.getEndDate(),
                s.getNextBillingDate(), s.getCreatedAt());
    }
}
