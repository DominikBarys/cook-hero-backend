package com.barysdominik.tutorialservice.entity.tutorial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialParametersDTO {
    private Boolean hasMeat;
    private Boolean isVeganRecipe;
    private Boolean isSpicyRecipe;
    private Boolean isSweetRecipe;
}
