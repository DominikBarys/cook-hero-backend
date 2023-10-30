package com.barysdominik.notificationservice.entity.notification;

import com.barysdominik.notificationservice.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(generator = "notification_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notification_id_seq", allocationSize = 1)
    private long id;
    private String shortId;
    private LocalDate creationDate;
    @Column(columnDefinition = "varchar(1024)")
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private boolean isChecked;
    @ManyToOne
    @JoinColumn(name = "receiverId", referencedColumnName = "id")
    private User receiver;
}
