package com.barysdominik.auth.entity.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class UserRegisterDTO {
    @Length(min = 5, max = 30, message = "Login musi zawierać od 5 do 30 znaków")
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(min = 8, max = 50, message = "Hasło musi zawierać od 8 do 50 znaków")
    private String password;
    @Email(message = "Błedny adres email")
    private String email;
    private Role role;
    private Rank rank;
}
