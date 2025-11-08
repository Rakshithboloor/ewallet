package com.hcl.ewallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.ewallet.dto.PaymentInitiateRequest;
import com.hcl.ewallet.dto.PaymentResponse;
import com.hcl.ewallet.dto.WalletBalanceResponse;
import com.hcl.ewallet.entity.Otp;
import com.hcl.ewallet.entity.Transaction;
import com.hcl.ewallet.exception.BusinessException;
import com.hcl.ewallet.gateway.WalletClient;
import com.hcl.ewallet.repository.OtpRepository;
import com.hcl.ewallet.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

// src/main/java/com/hcl/payment/service/PaymentService.java
@Service
@Transactional
@Slf4j
public class PaymentService {
Logger logger;
    private  TransactionRepository transactionRepo;
      WalletClient walletClient;
    private  OtpRepository otpRepository;

    private  ObjectMapper objectMapper = new ObjectMapper();
    private static final long OTP_VALID_DURATION_SECONDS = 60; // 1 minute
    public PaymentResponse initiatePayment(PaymentInitiateRequest request) {



        
            Optional<Otp> otpOptional = otpRepository.findByWalletIdAndOtpCode(request.getWalletId(),request.getSecretcode());

            if (otpOptional.isEmpty()) {
                throw new BusinessException("Otp is invalid: " + request );
            }  Duration elapsed = Duration.between(otpOptional.orElseThrow().getExpiresAt(), LocalDateTime.now());
        if (elapsed.getSeconds() > OTP_VALID_DURATION_SECONDS) {

            log.info("OTP expired for walletId={} after {} seconds", request.getSecretcode(), elapsed.getSeconds());
            throw new BusinessException("Otp is invalid: " + request );
        }
        WalletBalanceResponse wallet = walletClient.getBalance(request.getWalletId());

        BigDecimal total = request.getAmount().add(new BigDecimal("10.00"));
        if (wallet.getBalance().compareTo(total) < 0) {
            logger.info("Initiate Payment Failed. Wallet Balance is {}", wallet.getBalance());
            throw new BusinessException("Insufficient balance. Required: " + total + ", Available: " + wallet.getBalance());
        }

        Transaction tx = Transaction.builder()
                .walletId(request.getWalletId())
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .status("SUCCESS")
                .processedAt(LocalDateTime.now())
                .build();

        Map<String, Object> audit = Map.of(
                "event", "PAYMENT_SUCCESS",
                "balanceBefore", wallet.getWalletId(),
                "balanceAfter", wallet.getBalance().subtract(total),
                "fee", "10.00",
                "timestamp", LocalDateTime.now()
        );
        try {
            tx.setAuditTrail(objectMapper.writeValueAsString(audit));
        } catch (JsonProcessingException e) {
            tx.setAuditTrail("{}");
        }

        // 5. Save
        tx = transactionRepo.save(tx);

        // 6. Notify (fire-and-forget via async or Kafka in full version)
        // notifyMerchant(tx);

        return buildResponse(tx, "Payment processed successfully");
    }

    private PaymentResponse buildResponse(Transaction tx, String message) {
        BigDecimal total = tx.getAmount().add(tx.getWalletFee());
        return new PaymentResponse(
                tx.getId(),
                tx.getStatus(),
                total,
                tx.getWalletFee(),
                message,
                tx.getProcessedAt()
        );
    }
}