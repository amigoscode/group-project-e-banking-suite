package com.amogoscode.groupe.ebankingsuite.User.resquests;

public record UserRegistrationRequest(
        String fullName,
        String emailAddress,
        String password
) {
}
