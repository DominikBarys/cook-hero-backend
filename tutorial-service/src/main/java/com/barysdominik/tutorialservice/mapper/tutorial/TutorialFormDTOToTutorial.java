package com.barysdominik.tutorialservice.mapper.tutorial;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialFormDTO;
import com.barysdominik.tutorialservice.entity.user.User;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import com.barysdominik.tutorialservice.repository.DishRepository;
import com.barysdominik.tutorialservice.repository.IngredientRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TutorialFormDTOToTutorial {
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public Tutorial mapTutorialFormDTOToTutorial(TutorialFormDTO tutorialFormDTO) {
        Tutorial tutorial = new Tutorial();
        tutorial.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12)); //niepotrzebne
        tutorial.setName(tutorialFormDTO.getName());
        tutorial.setTimeToPrepare(tutorialFormDTO.getTimeToPrepare());
        tutorial.setDifficulty(tutorialFormDTO.getDifficulty());
        tutorial.setCreationDate(LocalDate.now()); //niepotrzebne
        tutorial.setImageUrls(tutorialFormDTO.getImagesUuid());
        tutorial.setShortDescription(tutorialFormDTO.getShortDescription());
        tutorial.setParameters(tutorialFormDTO.getParameters()); // gfd tutaj
        tutorial.setHasMeat(tutorialFormDTO.isHasMeat());
        tutorial.setVeganRecipe(tutorialFormDTO.isVeganRecipe());
        tutorial.setSweetRecipe(tutorialFormDTO.isSweetRecipe());
        tutorial.setSpicyRecipe(tutorialFormDTO.isSpicyRecipe());

        Category category = categoryRepository.findCategoryByShortId(tutorialFormDTO.getCategoryShortId()).orElse(null);
        tutorial.setCategory(category);
        Dish dish = dishRepository.findDishByShortId(tutorialFormDTO.getDishShortId()).orElse(null);
        tutorial.setDish(dish);

        List<Ingredient> ingredients = new ArrayList<>();
        if(tutorialFormDTO.getMainIngredientsShortIds() != null) {
            for(String ingredientShortId : tutorialFormDTO.getMainIngredientsShortIds()) {
                Ingredient ingredient = ingredientRepository.findIngredientByShortId(ingredientShortId).orElse(null);
                if(ingredient != null) {
                    ingredients.add(ingredient);
                }
            }
        }

        tutorial.setMainIngredients(ingredients);
        User user = userRepository.findUserByUuid(tutorialFormDTO.getAuthorUuid()).orElse(null);
        tutorial.setAuthor(user);

        return tutorial;
    }

}
