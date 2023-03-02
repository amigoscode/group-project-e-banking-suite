package com.amogoscode.group.ebankingsuite.user.resquests;

public record UserAuthenticationRequests(
        String emailAddress,
        String password
) {
}
