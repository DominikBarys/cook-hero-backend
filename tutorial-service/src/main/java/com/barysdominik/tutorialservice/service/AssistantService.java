package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.user.User;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import com.barysdominik.tutorialservice.repository.UserIngredientRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final UserIngredientRepository userIngredientRepository;
    private final UserRepository userRepository;
    private final TutorialRepository tutorialRepository;

    public List<Tutorial> getAssistantTutorials(String userUuid) throws ObjectDoesNotExistInDatabaseException {
        User user = userRepository.findUserByUuid(userUuid).orElse(null);
        if (user != null) {
            List<UserIngredient> userIngredients = userIngredientRepository.getAllByOwner(user);
            return matchTutorialsToUserIngredients(userIngredients);
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "User with uuid: '" + userUuid + "' does not exist in database"
        );
    }

    private List<Tutorial> matchTutorialsToUserIngredients(List<UserIngredient> userIngredients) {
        List<Tutorial> allTutorials = tutorialRepository.findAll();
        List<String> userIngredientsNames = new ArrayList<>();
        List<Tutorial> matchedTutorials = new ArrayList<>();

        for(UserIngredient userIngredient : userIngredients) {
            userIngredientsNames.add(userIngredient.getIngredient().getName());
        }

        for (Tutorial tutorial : allTutorials) {
            List<String> tutorialIngredientsNames = tutorial.getMainIngredients().stream()
                    .map(Ingredient::getName)
                    .toList();
            for(String tutorialIngredientName : tutorialIngredientsNames) {
                for(String userIngredientName : userIngredientsNames) {
                    if (userIngredientName.equals(tutorialIngredientName)) {
                        matchedTutorials.add(tutorial);
                    }
                }
            }
        }
        return matchedTutorials;
    }

}
