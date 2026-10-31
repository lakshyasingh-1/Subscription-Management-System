package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.QuotationRequest;
import com.subscriptiq.subscriptiq.dto.QuotationResponse;
import com.subscriptiq.subscriptiq.entity.Customer;
import com.subscriptiq.subscriptiq.entity.Plan;
import com.subscriptiq.subscriptiq.entity.Quotation;
import com.subscriptiq.subscriptiq.enums.QuotationStatus;
import com.subscriptiq.subscriptiq.repository.CustomerRepository;
import com.subscriptiq.subscriptiq.repository.PlanRepository;
import com.subscriptiq.subscriptiq.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;

    public QuotationService(QuotationRepository quotationRepository,
            CustomerRepository customerRepository, PlanRepository planRepository) {
        this.quotationRepository = quotationRepository;
        this.customerRepository = customerRepository;
        this.planRepository = planRepository;
    }

    @Transactional
    public QuotationResponse createQuotation(QuotationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        if (!plan.isActive()) {
            throw new IllegalArgumentException("Plan is inactive");
        }
        if (request.getValidUntil().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Validity date cannot be in the past");
        }

        BigDecimal unitPrice = plan.getPrice();
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        Quotation quotation = new Quotation();
        quotation.setQuotationNumber("QUO-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        quotation.setCustomer(customer);
        quotation.setPlan(plan);
        quotation.setQuantity(request.getQuantity());
        quotation.setUnitPrice(unitPrice);
        quotation.setSubtotal(subtotal);
        quotation.setStatus(QuotationStatus.DRAFT);
        quotation.setValidUntil(request.getValidUntil());
        quotation.setNotes(request.getNotes());

        return toResponse(quotationRepository.save(quotation));
    }

    @Transactional(readOnly = true)
    public List<QuotationResponse> getAllQuotations() {
        return quotationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public QuotationResponse getQuotationById(Long id) {
        return toResponse(findQuotation(id));
    }

    @Transactional(readOnly = true)
    public List<QuotationResponse> getByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        return quotationRepository.findByCustomerId(customerId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public QuotationResponse updateStatus(Long id, QuotationStatus newStatus) {
        Quotation quotation = findQuotation(id);
        if (quotation.getStatus() == QuotationStatus.ACCEPTED
                || quotation.getStatus() == QuotationStatus.REJECTED
                || quotation.getStatus() == QuotationStatus.EXPIRED) {
            throw new IllegalArgumentException("This quotation is already finalized");
        }
        if (quotation.getValidUntil() != null
                && quotation.getValidUntil().isBefore(LocalDate.now())
                && newStatus != QuotationStatus.EXPIRED) {
            quotation.setStatus(QuotationStatus.EXPIRED);
            throw new IllegalArgumentException("Quotation has expired");
        }
        quotation.setStatus(newStatus);
        return toResponse(quotationRepository.save(quotation));
    }

    private Quotation findQuotation(Long id) {
        return quotationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quotation not found"));
    }

    private QuotationResponse toResponse(Quotation q) {
        return new QuotationResponse(
                q.getId(), q.getQuotationNumber(),
                q.getCustomer().getId(), q.getCustomer().getFullName(),
                q.getPlan().getId(), q.getPlan().getName(),
                q.getQuantity(), q.getUnitPrice(), q.getSubtotal(),
                q.getStatus(), q.getValidUntil(), q.getNotes(), q.getCreatedAt());
    }
}
