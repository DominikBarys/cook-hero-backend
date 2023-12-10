package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.user.User;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {
    Optional<UserIngredient> getUserIngredientByShortId(String shortID);
    List<UserIngredient> getAllByOwner(User owner);
    List<UserIngredient> findUserIngredientsByIngredient(Ingredient ingredient);
}
