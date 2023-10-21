package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TutorialMediator {

    private final TutorialService tutorialService;

    public ResponseEntity<?> getTutorial(
            int page,
            int limit,
            String sort,
            String order,
            String uuid,
            String name,
            String dishUuid,
            String categoryUuid,
            String authorUuid,
            boolean hasMeat,
            boolean isVeganRecipe,
            boolean isSweetRecipe,
            boolean isSpicyRecipe
            ){
        long totalCount = tutorialService.countSearchedResults(
                name,
                dishUuid,
                categoryUuid,
                authorUuid,
                hasMeat,
                isVeganRecipe,
                isSweetRecipe,
                isSpicyRecipe
        );
        List<Tutorial> tutorials = tutorialService.getTutorial(
                page,
                limit,
                sort,
                order,
                uuid,
                name,
                dishUuid,
                categoryUuid,
                authorUuid,
                hasMeat,
                isVeganRecipe,
                isSweetRecipe,
                isSpicyRecipe
        );
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(totalCount)).body(tutorials);
    }
}
