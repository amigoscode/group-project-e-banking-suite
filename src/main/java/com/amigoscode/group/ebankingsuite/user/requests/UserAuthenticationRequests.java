package com.amigoscode.group.ebankingsuite.user.requests;

public record UserAuthenticationRequests(
        String emailAddress,
        String password
) {
}
