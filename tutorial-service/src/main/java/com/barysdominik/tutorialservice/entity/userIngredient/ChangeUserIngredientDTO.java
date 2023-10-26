package com.barysdominik.tutorialservice.entity.userIngredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserIngredientDTO {
    private String shortId;
    private LocalDate expirationDate;
    private Integer quantity;
}
