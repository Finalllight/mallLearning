package com.example.malllearning.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "余额查询响应")
public class BalanceResponse {

    @Schema(description = "账户余额", example = "1000.00")
    private BigDecimal balance;

    public BalanceResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
