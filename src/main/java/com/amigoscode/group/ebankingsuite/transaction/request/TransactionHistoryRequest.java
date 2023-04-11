package com.amigoscode.group.ebankingsuite.transaction.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TransactionHistoryRequest(
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "d/M/yyyy HH:mm:ss[.SSS][.SS][.S]")
        LocalDateTime startDateTime,
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "d/M/yyyy HH:mm:ss[.SSS][.SS][.S]")
        LocalDateTime endDateTime
) {
}
