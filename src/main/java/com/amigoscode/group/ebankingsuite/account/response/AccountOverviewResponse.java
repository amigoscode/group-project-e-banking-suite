package com.amigoscode.group.ebankingsuite.account.response;

import java.math.BigDecimal;

public record AccountOverviewResponse(
        BigDecimal accountBalance,
        String accountNumber,
        String TierLevel,
        String accountStatus
) {
}
