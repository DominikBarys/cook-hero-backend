package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredient(String shortId) {
        return ingredientRepository.findIngredientByShortId(shortId).orElse(null);
    }

    //TODO przypisanie ingredienta do usera i tworzenie ingredienta, mozliwe ze tylko 2 endpointy

}
