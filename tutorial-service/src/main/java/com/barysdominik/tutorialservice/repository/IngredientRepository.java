package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findIngredientByShortId(String shortId);
    Optional<Ingredient> findIngredientByName(String name);
}
