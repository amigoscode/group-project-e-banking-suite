package com.amigoscode.group.ebankingsuite.transaction.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NonNull;

import java.time.LocalDateTime;

public record TransactionHistoryRequest(
        @NonNull
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime startDateTime,

        @NonNull
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime endDateTime
) {
}
