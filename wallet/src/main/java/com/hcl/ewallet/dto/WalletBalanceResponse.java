package com.hcl.ewallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class WalletBalanceResponse {

    Long walletId;
    BigDecimal balance;
}