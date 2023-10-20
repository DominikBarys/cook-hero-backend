package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
}
