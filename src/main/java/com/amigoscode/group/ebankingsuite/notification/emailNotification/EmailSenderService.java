package com.amigoscode.group.ebankingsuite.notification.emailNotification;

import com.amigoscode.group.ebankingsuite.notification.emailNotification.request.FundsAlertNotificationRequest;

public interface EmailSenderService {

    void sendEmail(EmailNotification emailNotification);}
