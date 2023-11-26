package com.barysdominik.notificationservice.facade;

import com.barysdominik.notificationservice.entity.http.Response;
import com.barysdominik.notificationservice.exception.NotificationNotFoundException;
import com.barysdominik.notificationservice.mediator.NotificationMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationMediator notificationMediator;

    @GetMapping("/all")
    public ResponseEntity<?> getNotifications(@RequestParam String userUuid) {
        try {
            return notificationMediator.getAllNotifications(userUuid);
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error has occurred when getting a notifications");
        }
    }

    @GetMapping
    public ResponseEntity<?> getNotification(@RequestParam String shortId) {
        try {
            return notificationMediator.getNotification(shortId);
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error has occurred when getting a notifications");
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<Response> deleteAllNotifications(@RequestParam String userUuid) {
        return notificationMediator.deleteAllNotifications(userUuid);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteNotification(@RequestParam String shortId) {
        return notificationMediator.deleteNotification(shortId);
    }

    @PostMapping
    public ResponseEntity<Response> checkNotification(@RequestParam String shortId) {
        return notificationMediator.checkNotification(shortId);
    }

}
