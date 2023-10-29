package com.barysdominik.tutorialservice.entity.tutorial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorialFormDTO {
    private String name;
    private int timeToPrepare;
    private int difficulty;
    private String[] imagesUuid;
    private String shortDescription;
    private String parameters;
    private boolean hasMeat;
    private boolean isVeganRecipe;
    private boolean isSweetRecipe;
    private boolean isSpicyRecipe;
    private String dishShortId;
    private String[] mainIngredientsShortIds;
    private String categoryShortId;
    private String authorUuid;
}
