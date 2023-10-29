package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.ingredient.IngredientDTOToIngredient;
import com.barysdominik.tutorialservice.repository.IngredientRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import com.barysdominik.tutorialservice.repository.UserIngredientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientDTOToIngredient ingredientDTOToIngredient;
    private final TutorialRepository tutorialRepository;
    private final UserIngredientRepository userIngredientRepository;

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredient(String shortId) {
        return ingredientRepository.findIngredientByShortId(shortId).orElse(null);
    }

    public void createIngredient(IngredientDTO ingredientDTO) throws ObjectAlreadyExistInDatabaseException {
        ingredientRepository.findIngredientByName(ingredientDTO.getName()).ifPresent(value ->{
            throw new ObjectAlreadyExistInDatabaseException("Ingredient with this name already exists");
        });

        Ingredient ingredient = ingredientDTOToIngredient.mapIngredientDTOToIngredient(ingredientDTO);
        ingredient.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        ingredientRepository.save(ingredient);
    }

    @Transactional
    public void deleteIngredient(String shortId) throws ObjectDoesNotExistInDatabaseException{
        Ingredient ingredient = ingredientRepository.findIngredientByShortId(shortId).orElse(null);
        if (ingredient != null) {
            long ingredientID = ingredient.getId();
            List<Tutorial> tutorials = tutorialRepository.findTutorialsByIngredientId(ingredientID);
            for (Tutorial tutorial : tutorials) {
                List<Ingredient> tutorialMainIngredients = tutorial.getMainIngredients();
                tutorialMainIngredients.removeIf(value -> value.getName().equals(ingredient.getName()));
                tutorialRepository.save(tutorial);
            }
            List<UserIngredient> userIngredients = userIngredientRepository.findUserIngredientsByIngredient(ingredient);
            userIngredientRepository.deleteAll(userIngredients);
            ingredientRepository.delete(ingredient);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Ingredient with shortId: '" + shortId + "' does not exist in database"
        );
    }

}
