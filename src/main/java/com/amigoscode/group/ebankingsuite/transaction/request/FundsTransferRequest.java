package com.amigoscode.group.ebankingsuite.transaction.request;

import java.math.BigDecimal;

public record FundsTransferRequest(
        Integer receiverAccountId,
        Integer senderAccountId,
        BigDecimal amount,
        String transactionPin,
        String narration
) {
}
