package com.amigoscode.group.ebankingsuite.transaction.request;

import java.math.BigDecimal;

public record FundsTransferRequest(
        String receiverAccountNumber,
        String senderAccountNumber,
        BigDecimal amount,
        String transactionPin,
        String narration
) {
}
