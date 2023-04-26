package com.amigoscode.group.ebankingsuite.notification.smsNotification;

public record SmsNotification(
        String receiverPhoneNumber,
        String message
) {

}
