package com.amigoscode.group.ebankingsuite.user.requests;

public record ChangePasswordRequest (
        String oldPassword,
        String newPassword){
}
