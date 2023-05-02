package com.amigoscode.group.ebankingsuite.transaction.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NonNull;

import java.time.LocalDateTime;

public record TransactionHistoryRequest(
        @NonNull
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime startDateTime,

        @NonNull
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime endDateTime
) {
}
