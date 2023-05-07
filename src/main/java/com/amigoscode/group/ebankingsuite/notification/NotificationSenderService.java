package com.amigoscode.group.ebankingsuite.notification;

import com.amigoscode.group.ebankingsuite.notification.emailNotification.EmailNotification;
import com.amigoscode.group.ebankingsuite.notification.emailNotification.EmailSenderService;
import com.amigoscode.group.ebankingsuite.notification.emailNotification.request.FundsAlertNotificationRequest;
import com.amigoscode.group.ebankingsuite.notification.smsNotification.SmsNotification;
import com.amigoscode.group.ebankingsuite.notification.smsNotification.twilio.TwilioSmsSenderService;
import com.amigoscode.group.ebankingsuite.user.User;
import com.amigoscode.group.ebankingsuite.user.UserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationSenderService {

    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final TwilioSmsSenderService smsSenderService;

    public NotificationSenderService(UserService userService, EmailSenderService emailSenderService, TwilioSmsSenderService smsSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;

        this.smsSenderService = smsSenderService;
    }


    @Async
    public void sendCreditAndDebitNotification(FundsAlertNotificationRequest request){
        User senderUser = userService.getUserByUserId(request.senderId());
        User receiverUser = userService.getUserByUserId(request.receiverId());

        sendCreditDebitEmailAlertToCustomer(new CreditDebitEmailAlertTemplate(
                senderUser.getFullName(),
                receiverUser.getFullName(),
                senderUser.getEmailAddress(),
                receiverUser.getEmailAddress(),
                request.senderNewAccountBalance(),
                request.receiverNewAccountBalance(),
                request.amountTransferred()
        ));

        sendCreditDebitSmsAlertToCustomer(new CreditDebitSmsAlertTemplate(
                senderUser.getFullName(),
                receiverUser.getFullName(),
                senderUser.getPhoneNumber(),
                receiverUser.getPhoneNumber(),
                request.senderNewAccountBalance(),
                request.receiverNewAccountBalance(),
                request.amountTransferred()
        ));


    }

    public void sendCreditDebitEmailAlertToCustomer(CreditDebitEmailAlertTemplate template) {

        //credit alert for receiver
        final String receiverMessage = "Money In! You have been credited USD" + template.amountTransferred() +
                " from " + template.senderName() + ". You have USD" + template.receiverAccountBalance();

        emailSenderService.sendEmail(new EmailNotification(
                template.receiverEmailAddress(),
                "CREDIT ALERT",
                receiverMessage
        ));

        //debit alert for sender
        final String senderMessage = "Money Out! You have sent USD" + template.amountTransferred() +
                " to " + template.receiverName() + ". You have USD" + template.senderAccountBalance();
        emailSenderService.sendEmail(new EmailNotification(
                template.senderEmailAddress(),
                "DEBIT ALERT",
                senderMessage));
    }

    public void sendCreditDebitSmsAlertToCustomer(CreditDebitSmsAlertTemplate template){

        //credit alert for receiver
        final String receiverMessage = "Money Out! You have sent USD" + template.amountTransferred() +
                " to " + template.receiverName() + ". You have USD" + template.senderAccountBalance();

        smsSenderService.sendSms(new SmsNotification(
                template.receiverPhoneNumber(),
                receiverMessage
        ));

        //debit alert for sender
        final String senderMessage = "Money In! You have been credited USD" + template.amountTransferred() +
                " from " + template.senderName() + ". You have USD" + template.receiverAccountBalance();

        smsSenderService.sendSms(new SmsNotification(
                template.receiverPhoneNumber(),
                senderMessage
        ));
    }
}
