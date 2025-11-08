package com.hcl.ewallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// src/main/java/com/hcl/payment/entity/Otp.java
@Entity
@Table(name = "otps", indexes = {
        @Index(name = "idx_wallet", columnList = "wallet_id"),
        @Index(name = "idx_otp", columnList = "otp_code")
})
@Getter
@Setter
@NoArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Column(name = "otp_code", length = 6, nullable = false)
    private String otpCode;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}