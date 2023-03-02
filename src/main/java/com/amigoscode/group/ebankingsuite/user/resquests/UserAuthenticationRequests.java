package com.amigoscode.group.ebankingsuite.user.resquests;

public record UserAuthenticationRequests(
        String emailAddress,
        String password
) {
}
