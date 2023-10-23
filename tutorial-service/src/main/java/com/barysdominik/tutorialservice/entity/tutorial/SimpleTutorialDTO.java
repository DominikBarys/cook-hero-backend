package com.barysdominik.tutorialservice.entity.tutorial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTutorialDTO {
    //TODO add mapper
    private String name; //
    private int timeToPrepare;//
    private int difficulty;//
    private String imageUrl;//
    private String shortDescription;//
    private boolean hasMeat;//
    private boolean isVeganRecipe;//
    private boolean isSweetRecipe;//
    private boolean isSpicyRecipe;//
}
