package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    Optional<Tutorial> findTutorialByUuid(String uuid);
}
