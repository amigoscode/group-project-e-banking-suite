package com.amigoscode.group.ebankingsuite.account.request;

import lombok.NonNull;

public record AccountTransactionPinUpdateModel (
        @NonNull
        String transactionPin
){
}
