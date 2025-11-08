package com.hcl.ewallet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// src/main/java/com/hcl/payment/entity/Transaction.java
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_idempotency", columnList = "idempotencyKey", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walletId;
    private Long merchantId;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal amount;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal walletFee = BigDecimal.TEN; // fixed 10.00

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String status; // PENDING, SUCCESS, FAILED

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(columnDefinition = "JSON")
    private String auditTrail;

    private LocalDateTime initiatedAt;
    private LocalDateTime processedAt;

    @PrePersist
    void onCreate() {
        this.initiatedAt = LocalDateTime.now();
        this.status = "PENDING";
    }
}