package com.amigoscode.group.ebankingsuite.user.requests;

import lombok.NonNull;

public record ChangePasswordRequest (
        @NonNull
        String oldPassword,
        @NonNull
        String newPassword){
}
