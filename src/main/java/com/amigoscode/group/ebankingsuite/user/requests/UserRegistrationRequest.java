package com.amigoscode.group.ebankingsuite.user.requests;

import lombok.NonNull;

public record UserRegistrationRequest(
        @NonNull
        String fullName,
        @NonNull
        String emailAddress,
        @NonNull
        String password
) {
}
