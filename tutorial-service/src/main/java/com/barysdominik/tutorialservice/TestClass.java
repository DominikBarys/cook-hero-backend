package com.barysdominik.tutorialservice;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.repository.IngredientRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestClass {

    private final IngredientRepository ingredientRepository;
    private final TutorialRepository tutorialRepository;

    @PostConstruct
    public void test() {
//        Ingredient ingredient = ingredientRepository.findIngredientByShortId("2b4dbf62f8dc").orElse(null);
//        if (ingredient != null) {
//            long id = ingredient.getId();
//
//        }
        Ingredient ingredient = ingredientRepository.findIngredientByShortId("2b4dbf62f8dc").orElse(null);
        if (ingredient != null) {
            long ingredientID = ingredient.getId();
            List<Tutorial> tutorials = tutorialRepository.findTutorialsByIngredientId(ingredientID);
            for (Tutorial tutorial : tutorials) {
                List<Ingredient> tutorialMainIngredients = tutorial.getMainIngredients();
                tutorialMainIngredients.removeIf(value -> value.getName().equals(ingredient.getName()));
                System.out.println("siema");
            }
        }
    }

}
