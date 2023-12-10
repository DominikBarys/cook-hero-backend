package com.barysdominik.tutorialservice.mapper.useringredient;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserIngredientToUserIngredientDTO {
    public UserIngredientDTO mapUserIngredientToUserIngredientDTO(UserIngredient userIngredient) {
        UserIngredientDTO userIngredientDTO = new UserIngredientDTO();
        userIngredientDTO.setShortId(userIngredient.getShortId());
        userIngredientDTO.setIngredientDTO(createIngredientDTO(userIngredient.getIngredient()));
        userIngredientDTO.setExpirationDate(userIngredient.getExpirationDate());
        userIngredientDTO.setQuantity(userIngredient.getQuantity());
        userIngredientDTO.setUserUuid(userIngredient.getOwner().getUuid());
        return userIngredientDTO;
    }

    private IngredientDTO createIngredientDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setShortId(ingredient.getShortId());
        ingredientDTO.setName(ingredient.getName());
        return ingredientDTO;
    }
}
