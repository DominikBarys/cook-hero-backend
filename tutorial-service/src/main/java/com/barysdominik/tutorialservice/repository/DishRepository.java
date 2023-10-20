package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.dish.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
