package com.amigoscode.group.ebankingsuite.notification.emailNotification.gmail;

import com.amigoscode.group.ebankingsuite.notification.emailNotification.EmailNotification;
import com.amigoscode.group.ebankingsuite.notification.emailNotification.EmailSenderService;
import com.amigoscode.group.ebankingsuite.notification.emailNotification.request.FundsAlertNotificationRequest;
import com.amigoscode.group.ebankingsuite.user.User;
import com.amigoscode.group.ebankingsuite.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GmailEmailSenderService implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailNotification emailNotification) {
        try{
            // Creating a simple mail message object
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            String sender = "ebankingsuite@gmail.com";
            emailMessage.setFrom(sender);
            emailMessage.setTo(emailNotification.getReceiverEmail());
            emailMessage.setSubject(emailNotification.getSubject());
            emailMessage.setText(emailNotification.getMessage());
            mailSender.send(emailMessage);
        }catch (MailException e){
            System.out.println(e.getMessage());
        }
    }

}
