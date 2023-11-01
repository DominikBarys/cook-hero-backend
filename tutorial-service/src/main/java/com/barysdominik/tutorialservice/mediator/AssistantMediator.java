package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.tutorial.SimpleTutorialDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.tutorial.TutorialToSimpleTutorialDTO;
import com.barysdominik.tutorialservice.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssistantMediator {

    private final AssistantService assistantService;
    private final TutorialToSimpleTutorialDTO tutorialToSimpleTutorialDTO;

    public ResponseEntity<List<SimpleTutorialDTO>> getAssistantTutorials(String userUuid) {
        try {
            List<Tutorial> matchedTutorials = assistantService.getAssistantTutorials(userUuid);
            List<SimpleTutorialDTO> matchedTutorialsDTOS = new ArrayList<>();
            matchedTutorials.forEach(value -> {
                matchedTutorialsDTOS.add(tutorialToSimpleTutorialDTO.mapTutorialToSimpleTutorialDTO(value));
            });
            int totalCount = matchedTutorials.size();
            return ResponseEntity.ok()
                    .header("X-Total-Count", String.valueOf(totalCount))
                    .body(matchedTutorialsDTOS);
        } catch (ObjectDoesNotExistInDatabaseException e) {
            throw new ObjectDoesNotExistInDatabaseException(
                    "User with uuid: '" + userUuid + "' does not exist in database"
            );
        }
    }

}
