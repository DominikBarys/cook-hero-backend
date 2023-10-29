package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.tutorial.SpecialParametersDTO;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialFormDTO;
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
            @RequestParam(required = false) String name_like,//
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

    @PatchMapping("change-thumbnail")
    public ResponseEntity<Response> changeTutorialThumbnail(
            @RequestParam String shortId,
            @RequestParam int newThumbnailPosition
    ) {
        return tutorialMediator.changeTutorialThumbnail(shortId, newThumbnailPosition);
    }

    //TODO to do sprawdzenia, wgl wszystkie operacje zwiazane z image do sprawdzenia
    @PatchMapping("change-images")
    public ResponseEntity<Response> changeTutorialCarouselImages(
            @RequestParam String shortId,
            @RequestParam String[] newImages
    ) {
        return tutorialMediator.changeTutorialCarouselImages(shortId, newImages);
    }

    @PatchMapping("change-name")
    public ResponseEntity<Response> changeTutorialName(
            @RequestParam String shortId,
            @RequestParam String name
    ) {
        return tutorialMediator.changeTutorialName(shortId, name);
    }

    @PatchMapping("change-category")
    public ResponseEntity<Response> changeTutorialCategory(
            @RequestParam String shortId,
            @RequestParam String categoryShortId
    ) {
        return tutorialMediator.changeTutorialCategory(shortId, categoryShortId);
    }

    @PatchMapping("change-dish")
    public ResponseEntity<Response> changeTutorialDish(
            @RequestParam String shortId,
            @RequestParam String dishShortId
    ) {
        return tutorialMediator.changeTutorialDish(shortId, dishShortId);
    }

    //TODO this actually will set whole new array of ingredients, if we want to add, first we need to copy old ingredients
    @PatchMapping("/add-ingredients")
    public ResponseEntity<Response> addMainIngredientsToTutorial(
            @RequestBody List<IngredientDTO> ingredientDTOList,
            @RequestParam String tutorialShortId
    ) {
            return tutorialMediator.addMainIngredientsToTutorial(ingredientDTOList, tutorialShortId);
    }

    @PatchMapping("change-parameters")
    public ResponseEntity<Response> changeTutorialParameters(
            @RequestParam String shortId,
            @RequestParam String newParameters
    ) {
        return tutorialMediator.changeTutorialParameters(shortId, newParameters);
    }

    @PatchMapping("change-time-to-prepare")
    public ResponseEntity<Response> changeTimeToPrepare(
            @RequestParam String shortId,
            @RequestParam int newTimeToPrepare
    ) {
        return tutorialMediator.changeTutorialTimeToPrepare(shortId, newTimeToPrepare);
    }

    @PatchMapping("change-difficulty")
    public ResponseEntity<Response> changeDifficulty(
            @RequestParam String shortId,
            @RequestParam int newDifficulty
    ) {
        return tutorialMediator.changeTutorialDifficulty(shortId, newDifficulty);
    }

    @PatchMapping("change-short-description")
    public ResponseEntity<Response> changeShortDescription(
            @RequestParam String shortId,
            @RequestParam String newShortDescription
    ) {
        return tutorialMediator.changeTutorialShortDescription(shortId, newShortDescription);
    }

    @PatchMapping("change-special-parameters")
    public ResponseEntity<Response> changeSpecialParameters(
            @RequestParam String shortId,
            @RequestBody SpecialParametersDTO specialParametersDTO
    ) {
        return tutorialMediator.changeTutorialSpecialParameters(shortId, specialParametersDTO);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteTutorial(@RequestParam String shortId) {
        return tutorialMediator.deleteTutorial(shortId);
    }

}
