package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    Optional<Tutorial> findTutorialByShortId(String shortId);

    List<Tutorial> findTutorialsByCategory(Category category);

    List<Tutorial> findTutorialsByDish(Dish dish);
}
