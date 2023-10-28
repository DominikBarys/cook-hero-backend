package com.barysdominik.notificationservice.entity.notification;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String shortId;
    private LocalDate creationDate;
    private String message;
    private String type;
    private Boolean isChecked;
    private String receiverUuid;
}
