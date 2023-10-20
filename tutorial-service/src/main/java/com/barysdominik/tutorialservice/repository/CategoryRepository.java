package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
