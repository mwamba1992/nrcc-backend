package tz.go.roadsfund.nrcc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SMS service for sending notifications
 * Note: This is a placeholder. Implement actual SMS gateway integration based on provider
 */
@Service
@Slf4j
public class SmsService {

    @Value("${sms.provider.url}")
    private String smsProviderUrl;

    @Value("${sms.provider.api-key}")
    private String apiKey;

    @Value("${sms.provider.sender-id}")
    private String senderId;

    public void sendSms(String phoneNumber, String message) {
        try {
            // TODO: Implement actual SMS gateway integration
            // Example using REST client to call SMS provider API
            log.info("SMS would be sent to: {} with message: {}", phoneNumber, message);

            // Placeholder implementation
            // In production, integrate with actual SMS gateway (e.g., Twilio, Africa's Talking, etc.)

        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", phoneNumber, e);
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    public void sendApplicationStatusNotification(String phoneNumber, String applicationNumber, String status) {
        String message = String.format(
                "NRCC: Your application %s status: %s. Login to view details.",
                applicationNumber, status
        );
        sendSms(phoneNumber, message);
    }
}
