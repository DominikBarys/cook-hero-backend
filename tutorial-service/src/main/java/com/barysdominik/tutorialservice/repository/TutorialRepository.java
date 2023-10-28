package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    Optional<Tutorial> findTutorialByShortId(String shortId);

    List<Tutorial> findTutorialsByCategory(Category category);

    List<Tutorial> findTutorialsByDish(Dish dish);

    @Query("SELECT t FROM Tutorial t JOIN t.mainIngredients i WHERE i.id = :ingredientId")
    List<Tutorial> findTutorialsByIngredientId(@Param("ingredientId") Long ingredientId);

}
