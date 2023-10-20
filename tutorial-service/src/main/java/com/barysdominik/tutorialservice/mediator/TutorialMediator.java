package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TutorialMediator {

    private final TutorialService tutorialService;

    public ResponseEntity<?> getTutorial(int page, int limit){
        long totalCount = tutorialService.countTutorials();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(totalCount)).body("");
    }

}
