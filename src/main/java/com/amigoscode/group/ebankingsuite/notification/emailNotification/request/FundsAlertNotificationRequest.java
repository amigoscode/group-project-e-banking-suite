package com.amigoscode.group.ebankingsuite.notification.emailNotification.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FundsAlertNotificationRequest(
        int senderId,
        int receiverId,
        BigDecimal senderNewAccountBalance,
        BigDecimal receiverNewAccountBalance,
        BigDecimal amountTransferred

) {
}
