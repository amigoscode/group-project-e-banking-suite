package com.amigoscode.group.ebankingsuite.notification.smsNotification.twilio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
@Getter@Setter
public class TwilioConfiguration {
    private String accountSid;
    private String authToken;
    private String trialNumber;
}
