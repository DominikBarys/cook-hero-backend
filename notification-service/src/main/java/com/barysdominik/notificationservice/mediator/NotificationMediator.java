package com.barysdominik.notificationservice.mediator;

import com.barysdominik.notificationservice.entity.http.Response;
import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.entity.notification.NotificationDTO;
import com.barysdominik.notificationservice.exception.NotificationNotFoundException;
import com.barysdominik.notificationservice.exception.UserNotFoundException;
import com.barysdominik.notificationservice.mapper.NotificationToNotificationDTO;
import com.barysdominik.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationMediator {
    private final NotificationService notificationService;
    private final NotificationToNotificationDTO notificationToNotificationDTO;

    public ResponseEntity<List<NotificationDTO>> getAllNotifications(String userUuid) throws UserNotFoundException {
        try {
            List<Notification> notifications = notificationService.getAllNotifications(userUuid);
            List<NotificationDTO> notificationDTOS = new ArrayList<>();

            for (Notification notification : notifications) {
                notificationDTOS.add(notificationToNotificationDTO.mapNotificationToNotificationDTO(notification));
            }

            return ResponseEntity.ok(notificationDTOS);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    public ResponseEntity<NotificationDTO> getNotification(String shortId) throws NotificationNotFoundException {
        try {
            Notification notification = notificationService.getNotification(shortId);
            NotificationDTO notificationDTO =
                    notificationToNotificationDTO.mapNotificationToNotificationDTO(notification);
            return ResponseEntity.ok(notificationDTO);
        } catch (NotificationNotFoundException e) {
            throw new NotificationNotFoundException();
        }
    }

    public ResponseEntity<Response> deleteAllNotifications(String userUuid) {
        try {
            notificationService.deleteAllNotifications(userUuid);
            return ResponseEntity.ok(
                    new Response(
                            "Notification associated with user with shortId: '" + userUuid + "' deleted successfully"
                    )
            );
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("An unexpected error has occurred when deleting all notifications")
            );
        }
    }

    public ResponseEntity<Response> deleteNotification(String shortId) {
        try {
            notificationService.deleteNotification(shortId);
            return ResponseEntity.ok(new Response("Notification with shortId: '" + shortId + "' deleted successfully"));
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Notification with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }

}
