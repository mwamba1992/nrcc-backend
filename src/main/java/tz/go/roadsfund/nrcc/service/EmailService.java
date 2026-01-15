package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Email service for sending notifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendApplicationStatusNotification(String to, String applicantName,
                                                  String applicationNumber, String status) {
        String subject = "NRCC Application Status Update - " + applicationNumber;
        String body = String.format(
                "Dear %s,\n\n" +
                "Your road reclassification application (Reference: %s) status has been updated to: %s\n\n" +
                "Please log in to the NRCC portal to view more details.\n\n" +
                "Best regards,\n" +
                "NRCC System\n" +
                "Roads Fund Board",
                applicantName, applicationNumber, status
        );
        sendEmail(to, subject, body);
    }
}
