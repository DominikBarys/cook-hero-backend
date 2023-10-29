package com.barysdominik.notificationservice.service;

import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.entity.user.User;
import com.barysdominik.notificationservice.exception.NotificationNotFoundException;
import com.barysdominik.notificationservice.exception.UserNotFoundException;
import com.barysdominik.notificationservice.repository.NotificationRepository;
import com.barysdominik.notificationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<Notification> getAllNotifications(String userUuid) throws UserNotFoundException {
        User user = userRepository.findUserByUuid(userUuid).orElse(null);
        if (user != null) {
            return notificationRepository.findAllByReceiver(user);
        }
        throw new UserNotFoundException("User with shortId: '" + userUuid + "' does not exist in database");
    }

    public Notification getNotification(String shortId) throws NotificationNotFoundException {
        Notification notification = notificationRepository.getNotificationByShortId(shortId).orElse(null);
        if (notification != null) {
            return notification;
        }
        throw new NotificationNotFoundException("Notification with shortId: '" + shortId + "' does not exist in database");
    }

    public void deleteNotification(String shortId) throws NotificationNotFoundException {
        Notification notification = notificationRepository.getNotificationByShortId(shortId).orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
            return;
        }
        throw new NotificationNotFoundException("Notification with shortId: '" + shortId + "' does not exist in database");
    }

    public void deleteAllNotifications(String userUuid) throws NotificationNotFoundException {
        User user = userRepository.findUserByUuid(userUuid).orElse(null);
        List<Notification> notification = notificationRepository.findAllByReceiver(user);
        if (notification != null) {
            notificationRepository.deleteAll(notification);
            return;
        }
        throw new NotificationNotFoundException("An unexpected error has occurred when deleting all notifications");
    }

    public void checkNotification(String shortId) {
        Notification notification = notificationRepository.getNotificationByShortId(shortId).orElse(null);
        if(notification != null) {
            notification.setChecked(true);
            notificationRepository.save(notification);
            return;
        }
        throw new NotificationNotFoundException("Notification with shortId: '" + shortId + "' does not exist in database");
    }

    public void createExpiredNotification(long userId, int quantity, String ingredientName, LocalDate expirationDate) {

    }



}
