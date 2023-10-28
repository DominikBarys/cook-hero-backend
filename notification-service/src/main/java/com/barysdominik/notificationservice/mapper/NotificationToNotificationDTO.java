package com.barysdominik.notificationservice.mapper;

import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.entity.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationToNotificationDTO {

    public NotificationDTO mapNotificationToNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setShortId(notification.getShortId());
        notificationDTO.setCreationDate(notification.getCreationDate());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setType(notification.getType().toString());
        notificationDTO.setIsChecked(notification.isChecked());
        notificationDTO.setReceiverUuid(notification.getReceiver().getUuid());
        return notificationDTO;
    }

}
