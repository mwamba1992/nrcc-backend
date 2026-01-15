package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.Notification;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.NotificationChannel;
import tz.go.roadsfund.nrcc.enums.NotificationStatus;
import tz.go.roadsfund.nrcc.repository.NotificationRepository;

import java.time.LocalDateTime;

/**
 * Service for managing notifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    public void sendApplicationStatusNotification(Application application, User recipient,
                                                  NotificationChannel channel, String message) {
        try {
            // Create notification record
            Notification notification = Notification.builder()
                    .application(application)
                    .recipient(recipient)
                    .channel(channel)
                    .subject("Application Status Update")
                    .message(message)
                    .status(NotificationStatus.PENDING)
                    .build();

            notificationRepository.save(notification);

            // Send notification based on channel
            boolean sent = false;
            switch (channel) {
                case EMAIL:
                    emailService.sendApplicationStatusNotification(
                            recipient.getEmail(),
                            recipient.getName(),
                            application.getApplicationNumber(),
                            application.getStatus().name()
                    );
                    sent = true;
                    break;
                case SMS:
                    if (recipient.getPhoneNumber() != null) {
                        smsService.sendApplicationStatusNotification(
                                recipient.getPhoneNumber(),
                                application.getApplicationNumber(),
                                application.getStatus().name()
                        );
                        sent = true;
                    }
                    break;
                case ALL:
                    emailService.sendApplicationStatusNotification(
                            recipient.getEmail(),
                            recipient.getName(),
                            application.getApplicationNumber(),
                            application.getStatus().name()
                    );
                    if (recipient.getPhoneNumber() != null) {
                        smsService.sendApplicationStatusNotification(
                                recipient.getPhoneNumber(),
                                application.getApplicationNumber(),
                                application.getStatus().name()
                        );
                    }
                    sent = true;
                    break;
                case PORTAL:
                    // Portal notification is just the database record
                    sent = true;
                    break;
            }

            // Update notification status
            if (sent) {
                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
            } else {
                notification.setStatus(NotificationStatus.FAILED);
                notification.setErrorMessage("Failed to send notification");
            }

            notificationRepository.save(notification);

        } catch (Exception e) {
            log.error("Failed to send notification for application: {}", application.getApplicationNumber(), e);
        }
    }
}
