package com.hcl.ewallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class PaymentResponse {
    Long transactionId;
    String status;
    BigDecimal totalDeducted;
    BigDecimal fee;
    String message;
    LocalDateTime timestamp;

    }

