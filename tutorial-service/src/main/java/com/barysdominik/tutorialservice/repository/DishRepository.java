package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.dish.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findDishByShortId(String shortId);
    Optional<Dish> findDishByName(String name);
}
