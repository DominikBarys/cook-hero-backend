package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.mediator.TutorialMediator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tutorial")
public class TutorialController {

    private final TutorialMediator tutorialMediator;

    public ResponseEntity<?> get(
            HttpServletRequest request,
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int limit
    ) {
       return tutorialMediator.getTutorial(page, limit);
    }

}
