package com.barysdominik.tutorialservice.entity.userIngredient;

import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserIngredientDTO {
    private String shortId;
    private IngredientDTO ingredientDTO;
    private LocalDate expirationDate;
    private int quantity;
    private String userUuid;
}
