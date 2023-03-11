package com.amigoscode.group.ebankingsuite.user.requests;

public record UserRegistrationRequest(
        String fullName,
        String emailAddress,
        String password
) {
}
