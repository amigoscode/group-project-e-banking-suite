package com.amigoscode.group.ebankingsuite.notification.smsNotification.twilio;

import com.twilio.Twilio;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {

    private final static Logger logger = LoggerFactory.getLogger(TwilioInitializer.class);
    private final TwilioConfiguration twilioConfiguration;

    public TwilioInitializer(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
        Twilio.init(twilioConfiguration.getAccountSid(),
                twilioConfiguration.getAuthToken());
        logger.info("Twilio initialization done..");
    }
}
