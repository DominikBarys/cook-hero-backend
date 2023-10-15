package com.barysdominik.auth.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private Role role;
    private Rank rank;
}
