package com.amigoscode.group.ebankingsuite.user.resquests;

public record UserRegistrationRequest(
        String fullName,
        String emailAddress,
        String password
) {
}
