package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
