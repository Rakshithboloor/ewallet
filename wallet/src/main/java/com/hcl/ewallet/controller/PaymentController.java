package com.hcl.ewallet.controller;

import com.hcl.ewallet.dto.PaymentInitiateRequest;
import com.hcl.ewallet.dto.PaymentResponse;
import com.hcl.ewallet.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// src/main/java/com/hcl/payment/controller/PaymentController.java
@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentController {
Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(
            @Valid @RequestBody PaymentInitiateRequest request
    ) {
        // Override with header if present

        logger.info("initiate payment request {}", request);
        PaymentResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(response);
    }
}