package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mediator.IngredientMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ingredient")
public class IngredientController {
    private final IngredientMediator ingredientMediator;

    @GetMapping("/all")
    public ResponseEntity<List<IngredientDTO>> getIngredients() {
        return ingredientMediator.getIngredients();
    }

    @GetMapping
    public ResponseEntity<?> getIngredient(@RequestParam String shortId) {
        try {
            return ingredientMediator.getIngredient(shortId);
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Ingredient with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createIngredient(@RequestBody IngredientDTO ingredientDTO) {
        try {
            ingredientMediator.createIngredient(ingredientDTO);
        } catch (ObjectAlreadyExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Category with this name already exist")
            );
        }
        return ResponseEntity.ok(new Response("Operation ended with success"));
    }

}
