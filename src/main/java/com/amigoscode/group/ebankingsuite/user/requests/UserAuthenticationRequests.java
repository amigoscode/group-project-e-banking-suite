package com.amigoscode.group.ebankingsuite.user.requests;

import lombok.NonNull;

public record UserAuthenticationRequests(
        @NonNull
        String emailAddress,
        @NonNull
        String password
) {
}
