package com.barysdominik.notificationservice.mapper;

import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.entity.notification.NotificationDTO;
import com.barysdominik.notificationservice.entity.notification.NotificationType;
import com.barysdominik.notificationservice.entity.user.User;
import com.barysdominik.notificationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDTOToNotification {

    private final UserRepository userRepository;

    public Notification mapNotificationDTOToNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setShortId(notificationDTO.getShortId());
        notification.setCreationDate(notificationDTO.getCreationDate());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(NotificationType.valueOf(notificationDTO.getType()));
        notification.setChecked(notificationDTO.getIsChecked());

        User user = userRepository.findUserByUuid(notificationDTO.getReceiverUuid()).orElse(null);
        notification.setReceiver(user);

        return notification;
    }

}
