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
            @RequestParam(required = false,defaultValue = "1") int _page,
            @RequestParam(required = false,defaultValue = "5") int _limit,
            @RequestParam(required = false, defaultValue = "creationDate") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order,
            @RequestParam(required = false) String _uuid,
            @RequestParam(required = false) String name_like,
            @RequestParam(required = false) String _dish,
            @RequestParam(required = false) String _category,
            @RequestParam(required = false) String _author,
            @RequestParam(required = false) Boolean _hasMeat,
            @RequestParam(required = false) Boolean _isVeganRecipe,
            @RequestParam(required = false) Boolean _isSweetRecipe,
            @RequestParam(required = false) Boolean _isSpicyRecipe
            ) {
       return tutorialMediator.getTutorial(
               _page,
               _limit,
               _sort,
               _order,
               _uuid,
               name_like,
               _dish,
               _category,
               _author,
               _hasMeat,
               _isVeganRecipe,
               _isSweetRecipe,
               _isSpicyRecipe
       );
    }

}
