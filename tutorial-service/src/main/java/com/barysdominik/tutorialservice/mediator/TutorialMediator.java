package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.tutorial.SimpleTutorialDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialDTO;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialFormDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.tutorial.TutorialFormDTOToTutorial;
import com.barysdominik.tutorialservice.mapper.tutorial.TutorialToSimpleTutorialDTO;
import com.barysdominik.tutorialservice.mapper.tutorial.TutorialToTutorialDTO;
import com.barysdominik.tutorialservice.service.CategoryService;
import com.barysdominik.tutorialservice.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TutorialMediator {

    private final TutorialService tutorialService;
    private final TutorialToSimpleTutorialDTO tutorialToSimpleTutorialDTO;
    private final CategoryService categoryService;
    private final TutorialToTutorialDTO tutorialToTutorialDTO;
    private final TutorialFormDTOToTutorial tutorialFormDTOToTutorial;
    @Value("${external.url.file-service}")
    private String FILE_SERVICE_EXTERNAL_URL;

    public ResponseEntity<?> getTutorial(
            Integer page,
            Integer limit,
            String sort,
            String order,
            String shortId,
            String name,
            String dishShortId,
            String categoryShortId,
            String authorUuid,
            Boolean hasMeat,
            Boolean isVeganRecipe,
            Boolean isSweetRecipe,
            Boolean isSpicyRecipe
            ){
        if(name != null && name.trim().isEmpty()) {
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        List<Tutorial> tutorials = tutorialService.getTutorial(
                page,
                limit,
                sort,
                order,
                shortId,
                name,
                dishShortId,
                categoryShortId,
                authorUuid,
                hasMeat,
                isVeganRecipe,
                isSweetRecipe,
                isSpicyRecipe
        );

        tutorials.forEach(value -> {
            for(int i = 0; i < value.getImageUrls().length; i++) {
                value.getImageUrls()[i] = FILE_SERVICE_EXTERNAL_URL + "?shortId" + value.getImageUrls()[i];
            }
        });

        if(shortId == null || shortId.trim().isEmpty()) {
            List<SimpleTutorialDTO> simpleTutorialDTOS = new ArrayList<>();
            long totalCount = tutorialService.countSearchedResults(
                    name,
                    dishShortId,
                    categoryShortId,
                    authorUuid,
                    hasMeat,
                    isVeganRecipe,
                    isSweetRecipe,
                    isSpicyRecipe
            );
            tutorials.forEach(
                    value -> simpleTutorialDTOS.add(tutorialToSimpleTutorialDTO.mapTutorialToSimpleTutorialDTO(value))
            );
            return ResponseEntity.ok().header(
                    "X-Total-Count",
                    String.valueOf(totalCount)).body(simpleTutorialDTOS
            );
        }
        TutorialDTO tutorialDTO = tutorialToTutorialDTO.mapTutorialToTutorialDTO(tutorials.get(0));
        return ResponseEntity.ok().body(tutorialDTO);
    }

    public ResponseEntity<Response> saveTutorial(TutorialFormDTO tutorialFormDTO) {
        try {
            Tutorial tutorial = tutorialFormDTOToTutorial.mapTutorialFormDTOToTutorial(tutorialFormDTO);
//            categoryService.getCategory(tutorial.getCategory().getShortId()).ifPresentOrElse(
//                    tutorial::setCategory,
//                    () -> {
//                        throw new ObjectDoesNotExistInDatabaseException("This type of category does not exist in database");
//                    }
//            );
            tutorialService.createTutorial(tutorial);
            return ResponseEntity.ok(new Response("Tutorial created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("This type of category does not exist in database"));
        }
    }

    public ResponseEntity<Response> addMainIngredientsToTutorial(
            List<IngredientDTO> ingredientDTOS,
            String tutorialShortId
    ) {
        try {
            tutorialService.addMainIngredientsToTutorial(ingredientDTOS, tutorialShortId);
            return ResponseEntity.ok(new Response("Main ingredients were added to tutorial successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Problem has occurred while adding main ingredients to tutorial")
            );
        }
    }

    public ResponseEntity<Response> deleteTutorial(String shortId) {
        try {
            tutorialService.delete(shortId);
            return ResponseEntity.ok(new Response("Tutorial deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Such tutorial does not exist in database"));
        }
    }
}
