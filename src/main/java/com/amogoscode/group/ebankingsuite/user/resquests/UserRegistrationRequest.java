package com.amogoscode.group.ebankingsuite.user.resquests;

public record UserRegistrationRequest(
        String fullName,
        String emailAddress,
        String password
) {
}
