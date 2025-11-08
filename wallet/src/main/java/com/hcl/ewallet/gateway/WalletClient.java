package com.hcl.ewallet.gateway;

import com.hcl.ewallet.dto.WalletBalanceResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "wallet-service", url    = "${wallet.service.url:http://localhost:8081}")
@CircuitBreaker(name = "walletService", fallbackMethod = "fallbackResponse")
public interface WalletClient {
    @GetMapping("/wallets/{id}")
    WalletBalanceResponse getBalance(@PathVariable("id") Long walletId);
}
