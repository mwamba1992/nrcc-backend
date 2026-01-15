package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.Notification;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.NotificationChannel;
import tz.go.roadsfund.nrcc.enums.NotificationStatus;

import java.util.List;

/**
 * Repository interface for Notification entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient(User recipient);

    List<Notification> findByRecipientId(Long recipientId);

    List<Notification> findByApplication(Application application);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByChannel(NotificationChannel channel);

    List<Notification> findByRecipientAndStatus(User recipient, NotificationStatus status);

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}
