package com.subscriptiq.subscriptiq.controller;

import com.subscriptiq.subscriptiq.dto.BillingPreviewDTO;
import com.subscriptiq.subscriptiq.dto.BillingResultDTO;
import com.subscriptiq.subscriptiq.dto.BillingRunRequestDTO;
import com.subscriptiq.subscriptiq.service.BillingRunService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing-runs")
public class BillingRunController {

    private final BillingRunService billingRunService;

    public BillingRunController(BillingRunService billingRunService) {
        this.billingRunService = billingRunService;
    }

    @GetMapping("/preview")
    public List<BillingPreviewDTO> previewBillingRun() {
        return billingRunService.getBillingPreview();
    }

    @PostMapping("/run")
    public BillingResultDTO runBilling(
            @RequestBody BillingRunRequestDTO request) {

        return billingRunService.runBilling(request);
    }

    @GetMapping("/result/{id}")
    public BillingResultDTO billingResult(
            @PathVariable Long id) {

        return billingRunService.getBillingResult(id);
    }
}