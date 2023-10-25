package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialFormDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mediator.TutorialMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tutorial")
public class TutorialController {

    private final TutorialMediator tutorialMediator;

    @GetMapping
    public ResponseEntity<?> get(
            @RequestParam(required = false, defaultValue = "1") int _page,
            @RequestParam(required = false, defaultValue = "5") int _limit,
            @RequestParam(required = false, defaultValue = "creationDate") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order,
            @RequestParam(required = false) String _shortId,
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
                _shortId,
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

    @PostMapping
    public ResponseEntity<Response> saveTutorial(@RequestBody TutorialFormDTO tutorialDTO) {
        return tutorialMediator.saveTutorial(tutorialDTO);
    }

    @PatchMapping("/add-ingredients")
    public ResponseEntity<Response> addMainIngredientsToTutorial(
            @RequestBody List<IngredientDTO> ingredientDTOList,
            @RequestParam String tutorialShortId
    ) {
            return tutorialMediator.addMainIngredientsToTutorial(ingredientDTOList, tutorialShortId);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteTutorial(@RequestParam String shortId) {
        return tutorialMediator.deleteTutorial(shortId);
    }

}
