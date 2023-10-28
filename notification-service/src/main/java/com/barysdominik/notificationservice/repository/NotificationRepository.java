package com.barysdominik.notificationservice.repository;

import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiver(User receiver);

    Optional<Notification> getNotificationByShortId(String shortId);

}
