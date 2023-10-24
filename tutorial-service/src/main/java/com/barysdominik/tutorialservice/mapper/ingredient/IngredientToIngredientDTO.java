package com.barysdominik.tutorialservice.mapper.ingredient;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientDTO {

    public IngredientDTO mapIngredientToIngredientDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setName(ingredient.getName());
        ingredientDTO.setShortId(ingredient.getShortId());
        return ingredientDTO;
    }

}
