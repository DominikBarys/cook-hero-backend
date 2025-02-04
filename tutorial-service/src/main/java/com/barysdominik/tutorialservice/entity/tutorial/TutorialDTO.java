package com.barysdominik.tutorialservice.entity.tutorial;

import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.entity.dish.DishDTO;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorialDTO {
    private String name;
    private String shortId;
    private int timeToPrepare;
    private int difficulty;
    private LocalDate creationDate;
    private String[] imageUrls;
    private String shortDescription;
    private String parameters;
    private DishDTO dishDTO;
    private List<IngredientDTO> mainIngredientsDTOS;
    private CategoryDTO categoryDTO;
    private UserDTO authorDTO;
    private boolean hasMeat;
    private boolean isVeganRecipe;
    private boolean isSweetRecipe;
    private boolean isSpicyRecipe;
}
