package com.amigoscode.group.ebankingsuite.transaction.request;

import lombok.NonNull;

import java.math.BigDecimal;

public record FundsTransferRequest(
        @NonNull
        String receiverAccountNumber,
        @NonNull
        BigDecimal amount,
        @NonNull
        String transactionPin,
        String narration
) {
}
