package com.barysdominik.auth.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String uuid;
    private String username;
    private String email;
    private String role;
    private String rank;
    private LocalDate joinedAt;
    private int amountOfCreatedTutorials;
}
