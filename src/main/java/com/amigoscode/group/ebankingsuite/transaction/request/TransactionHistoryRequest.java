package com.amigoscode.group.ebankingsuite.transaction.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NonNull;

import java.time.LocalDateTime;

public record TransactionHistoryRequest(
        @NonNull
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "d/M/yyyy HH:mm:ss[.SSS][.SS][.S]")
        LocalDateTime startDateTime,

        @NonNull
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "d/M/yyyy HH:mm:ss[.SSS][.SS][.S]")
        LocalDateTime endDateTime
) {
}
