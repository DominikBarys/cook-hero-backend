package com.barysdominik.notificationservice.service;

import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.entity.notification.NotificationType;
import com.barysdominik.notificationservice.entity.user.User;
import com.barysdominik.notificationservice.exception.NotificationNotFoundException;
import com.barysdominik.notificationservice.exception.UserNotFoundException;
import com.barysdominik.notificationservice.repository.NotificationRepository;
import com.barysdominik.notificationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    public void checkNotification(String shortId) throws NotificationNotFoundException{
        Notification notification = notificationRepository.getNotificationByShortId(shortId).orElse(null);
        if (notification != null) {
            notification.setChecked(true);
            notificationRepository.save(notification);
            return;
        }
        throw new NotificationNotFoundException("Notification with shortId: '" + shortId + "' does not exist in database");
    }

    public void createExpiredNotification(long userId, int quantity, String ingredientName, LocalDate expirationDate)
            throws UserNotFoundException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification notification = new Notification();
            notification.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            notification.setCreationDate(LocalDate.now());
            notification.setType(NotificationType.CRITICAL);
            notification.setReceiver(user);

            String notificationMessage = "Składnik '" + ingredientName + "' w ilości " + quantity +
                    "został przeterminowany :( Miał datę ważności do: " + expirationDate +
                    ". Zalecamy pozbyć się tego składnika i najlepiej usunąć go również z wirtualnej lodówki.";

            notification.setMessage(notificationMessage);
            notificationRepository.save(notification);

            return;
        }
        throw new UserNotFoundException("User was not found");
    }

    public void createCloseToExpirationNotification(
            long userId,
            int quantity,
            String ingredientName,
            LocalDate expirationDate
    ) throws UserNotFoundException {

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification notification = new Notification();
            notification.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            notification.setCreationDate(LocalDate.now());
            notification.setType(NotificationType.WARNING);
            notification.setReceiver(user);

            String notificationMessage = "Składnik '" + ingredientName + "' w ilości " + quantity +
                    " jest blisko przeterminowania. Data przeterminowania: " + expirationDate +
                    ". Proponujemy skorzystać z naszego asystenta dopasowującego idealne danie do twoich składników" +
                    " z naszej bazy poradników lub samodzielnie wyszukać odpowiedni poradnik korzystając z" +
                    " dostępnych filtrów";

            notification.setMessage(notificationMessage);
            notificationRepository.save(notification);

            return;
        }
        throw new UserNotFoundException("User was not found");

    }


}
