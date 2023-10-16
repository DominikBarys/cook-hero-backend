package com.barysdominik.auth.entity.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegisterDTO {
    //tutaj dodac potrzebne walidatory
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String email;
    private Role role;
    private Rank rank;
}
