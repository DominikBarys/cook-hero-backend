package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.page.Page;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> getPagesByTutorial(Tutorial tutorial);
    Optional<Page> findPageByShortId(String shortId);
    long countPagesByTutorial(Tutorial tutorial);
    List<Page> getAllByTutorial(Tutorial tutorial);

}
