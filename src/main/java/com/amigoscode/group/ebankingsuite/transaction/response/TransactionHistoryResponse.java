package com.amigoscode.group.ebankingsuite.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TransactionHistoryResponse {
    LocalDateTime transactionDateTime;
    BigDecimal amount;
    TransactionType transactionType;
    String SenderName;
    String receiverName;
    String description;
}
