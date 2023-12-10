package com.barysdominik.tutorialservice.mapper.tutorial;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.dish.DishDTO;
import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialDTO;
import com.barysdominik.tutorialservice.entity.user.User;
import com.barysdominik.tutorialservice.entity.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TutorialToTutorialDTO {

    public TutorialDTO mapTutorialToTutorialDTO(Tutorial tutorial) {
        TutorialDTO tutorialDTO = new TutorialDTO();
        tutorialDTO.setShortId(tutorial.getShortId());
        tutorialDTO.setTimeToPrepare(tutorial.getTimeToPrepare());
        tutorialDTO.setDifficulty(tutorial.getDifficulty());
        tutorialDTO.setImageUrls(tutorial.getImageUrls());
        tutorialDTO.setShortDescription(tutorial.getShortDescription());
        tutorialDTO.setParameters(tutorial.getParameters());
        tutorialDTO.setCreationDate(tutorial.getCreationDate());
        if(tutorial.getDish() != null) {
            tutorialDTO.setDishDTO(createDishDTO(tutorial.getDish()));
        }
        if(tutorial.getMainIngredients() != null) {
            tutorialDTO.setMainIngredientsDTOS(createIngredientsDTOS(tutorial.getMainIngredients()));
        }
        if(tutorial.getCategory() != null) {
            tutorialDTO.setCategoryDTO(createCategoryDTO(tutorial.getCategory()));
        }
        if(tutorial.getAuthor() != null) {
            tutorialDTO.setAuthorDTO(createUserDTO(tutorial.getAuthor()));
        }

        tutorialDTO.setName(tutorial.getName());
        tutorialDTO.setHasMeat(tutorial.isHasMeat());
        tutorialDTO.setVeganRecipe(tutorial.isVeganRecipe());
        tutorialDTO.setSweetRecipe(tutorial.isSweetRecipe());
        tutorialDTO.setSpicyRecipe(tutorial.isSpicyRecipe());

        return tutorialDTO;
    }

    private CategoryDTO createCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setShortId(category.getShortId());
        return categoryDTO;
    }

    private DishDTO createDishDTO(Dish dish) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setShortId(dish.getShortId());
        dishDTO.setName(dish.getName());
        return dishDTO;
    }

    private UserDTO createUserDTO(User user) {
        UserDTO authorDTO = new UserDTO();
        authorDTO.setUuid(user.getUuid());
        authorDTO.setUsername(user.getUsername());
        return authorDTO;
    }

    private List<IngredientDTO> createIngredientsDTOS(List<Ingredient> ingredients) {
        List<IngredientDTO> ingredientDTOS = new ArrayList<>();
        IngredientDTO ingredientDTO = null;
        for (Ingredient ingredient : ingredients) {
            ingredientDTO = new IngredientDTO();
            ingredientDTO.setShortId(ingredient.getShortId());
            ingredientDTO.setName(ingredient.getName());
            ingredientDTOS.add(ingredientDTO);
        }
        return ingredientDTOS;
    }
}
