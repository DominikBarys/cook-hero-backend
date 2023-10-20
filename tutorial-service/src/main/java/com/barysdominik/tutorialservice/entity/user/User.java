package com.barysdominik.tutorialservice.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;
    private String uuid;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    private LocalDate joinedAt;
    private int amountOfCreatedTutorials;
    private int amountOfReviews;
    @Column(name = "islock", columnDefinition = "boolean default true")
    private boolean isLock;
    @Column(name = "isenabled", columnDefinition = "boolean default false")
    private boolean isEnabled;
}
