package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.http.CreateTutorialResponse;
import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.ingredient.IngredientDTO;
import com.barysdominik.tutorialservice.entity.tutorial.*;
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
    ) {
        if (name != null && name.trim().isEmpty()) {
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
            for (int i = 0; i < value.getImageUrls().length; i++) {
                value.getImageUrls()[i] = FILE_SERVICE_EXTERNAL_URL + "?shortId=" + value.getImageUrls()[i];
            }
        });

        if (shortId == null || shortId.trim().isEmpty()) {
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

    public ResponseEntity<CreateTutorialResponse> saveTutorial(TutorialFormDTO tutorialFormDTO) {
        try {
            Tutorial tutorial = tutorialFormDTOToTutorial.mapTutorialFormDTOToTutorial(tutorialFormDTO);
            tutorialService.createTutorial(tutorial);
            return ResponseEntity.ok(new CreateTutorialResponse("Tutorial created successfully", tutorial.getShortId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CreateTutorialResponse("Something went wrong while creating tutorial", null));
        }
    }

    public ResponseEntity<Response> changeTutorialThumbnail(String shortId, int newThumbnailPosition) {
        try {
            tutorialService.changeTutorialThumbnail(shortId, newThumbnailPosition);
            return ResponseEntity.ok(new Response("Thumbnail changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing thumbnail"));
        }
    }

    public ResponseEntity<Response> changeTutorialCarouselImages(String shortId, String[] newImages) {
        try {
            tutorialService.changeTutorialCarouselImages(shortId, newImages);
            return ResponseEntity.ok(new Response("Images in carousel changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing images"));
        }
    }

    public ResponseEntity<Response> changeTutorialName(String shortId, String name) {
        try {
            tutorialService.changeTutorialName(shortId, name);
            return ResponseEntity.ok(new Response("Name changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing name"));
        }
    }

    public ResponseEntity<Response> changeTutorialCategory(String shortId, String categoryShortId) {
        try {
            tutorialService.changeTutorialCategory(shortId, categoryShortId);
            return ResponseEntity.ok(new Response("Category changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing category"));
        }
    }

    public ResponseEntity<Response> changeTutorialDish(String shortId, String dishShortId) {
        try {
            tutorialService.changeTutorialDish(shortId, dishShortId);
            return ResponseEntity.ok(new Response("Dish changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing dish"));
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

    public ResponseEntity<Response> changeTutorialParameters(String shortId, String newParameters) {
        try {
            tutorialService.changeTutorialParameters(shortId, newParameters);
            return ResponseEntity.ok(new Response("Parameters changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing parameters"));
        }
    }

    public ResponseEntity<Response> changeTutorialTimeToPrepare(String shortId, int newTimeToPrepare) {
        try {
            tutorialService.changeTimeToPrepare(shortId, newTimeToPrepare);
            return ResponseEntity.ok(new Response("Time to prepare changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing time to prepare"));
        }
    }

    public ResponseEntity<Response> changeTutorialDifficulty(String shortId, int newDifficulty) {
        try {
            tutorialService.changeTutorialDifficulty(shortId, newDifficulty);
            return ResponseEntity.ok(new Response("Difficulty changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing difficulty"));
        }
    }

    public ResponseEntity<Response> changeTutorialShortDescription(String shortId, String newShortDescription) {
        try {
            tutorialService.changeShortDescription(shortId, newShortDescription);
            return ResponseEntity.ok(new Response("Short description changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing short description"));
        }
    }

    public ResponseEntity<Response> changeTutorialSpecialParameters(
            String shortId,
            SpecialParametersDTO specialParametersDTO
    ) {
        try {
            tutorialService.changeSpecialParameters(shortId, specialParametersDTO);
            return ResponseEntity.ok(new Response("Special parameters description changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing special parameters"));
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
