package com.amigoscode.group.ebankingsuite.notification.smsNotification.twilio;

import com.amigoscode.group.ebankingsuite.notification.smsNotification.SmsNotification;
import com.amigoscode.group.ebankingsuite.notification.smsNotification.SmsSenderService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSenderService implements SmsSenderService {

    private final TwilioConfiguration twilioConfiguration;

    public TwilioSmsSenderService(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendSms(SmsNotification smsNotification) {
        Message.creator(
                new PhoneNumber(smsNotification.receiverPhoneNumber()),
                new PhoneNumber(twilioConfiguration.getTrialNumber()),
                smsNotification.message()
        ).create();

    }
}
