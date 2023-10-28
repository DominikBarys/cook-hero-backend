package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.ingredient.IngredientToIngredientDTO;
import com.barysdominik.tutorialservice.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IngredientMediator {

    private final IngredientService ingredientService;
    private final IngredientToIngredientDTO ingredientToIngredientDTO;

    public ResponseEntity<List<IngredientDTO>> getIngredients() {
        List<IngredientDTO> ingredientDTOS = new ArrayList<>();
        ingredientService.getIngredients().forEach(value -> {
            ingredientDTOS.add(ingredientToIngredientDTO.mapIngredientToIngredientDTO(value));
        });
        return ResponseEntity.ok(ingredientDTOS);
    }

    public ResponseEntity<IngredientDTO> getIngredient(String shortId) {
        Ingredient ingredient = ingredientService.getIngredient(shortId);
        if(ingredient != null) {
            IngredientDTO ingredientDTO = ingredientToIngredientDTO.mapIngredientToIngredientDTO(ingredient);
            return ResponseEntity.ok(ingredientDTO);
        }
        throw new ObjectDoesNotExistInDatabaseException();
    }

    public void createIngredient(IngredientDTO ingredientDTO) throws ObjectAlreadyExistInDatabaseException {
        ingredientService.createIngredient(ingredientDTO);
    }

    public ResponseEntity<Response> deleteIngredient(String shortId) {
        try {
            ingredientService.deleteIngredient(shortId);
            return ResponseEntity.ok(new Response("Ingredient with shortId: '" + shortId + "' deleted successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Ingredient with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }
}
