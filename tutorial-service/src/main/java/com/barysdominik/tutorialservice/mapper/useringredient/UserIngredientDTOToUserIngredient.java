package com.barysdominik.tutorialservice.mapper.useringredient;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.user.User;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredientDTO;
import com.barysdominik.tutorialservice.repository.IngredientRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserIngredientDTOToUserIngredient {
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public UserIngredient mapUserIngredientDTOToUserIngredient(UserIngredientDTO userIngredientDTO) {
        UserIngredient userIngredient = new UserIngredient();
        userIngredient.setShortId(userIngredientDTO.getShortId());
        Ingredient ingredient = ingredientRepository.findIngredientByShortId(userIngredientDTO.getIngredientDTO()
                .getShortId())
                .orElse(null);
        userIngredient.setIngredient(ingredient);
        userIngredient.setExpirationDate(userIngredientDTO.getExpirationDate());
        userIngredient.setQuantity(userIngredientDTO.getQuantity());
        User user = userRepository.findUserByUuid(userIngredientDTO.getUserUuid()).orElse(null);
        userIngredient.setOwner(user);
        return userIngredient;
    }
}
