package com.barysdominik.tutorialservice.mapper.ingredient;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import org.springframework.stereotype.Component;

@Component
public class IngredientDTOToIngredient {

    public Ingredient mapIngredientDTOToIngredient(IngredientDTO ingredientDTO){
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName());
        ingredient.setShortId(ingredientDTO.getShortId());
        return ingredient;
    }

}
