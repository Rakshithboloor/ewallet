package com.hcl.ewallet.repository;

import com.hcl.ewallet.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByWalletIdAndOtpCode(Long walletId, String otpCode);
}