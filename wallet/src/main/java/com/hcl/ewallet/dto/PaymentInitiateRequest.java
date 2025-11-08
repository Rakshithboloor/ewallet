package com.hcl.ewallet.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
@Data
public class PaymentInitiateRequest {
    @NotNull
    Long walletId;
    @NotNull Long merchantId;
    @NotNull
    BigDecimal amount;
    @NotNull
    String secretcode;
}
