package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.user.User;
import com.barysdominik.tutorialservice.entity.userIngredient.ChangeUserIngredientDTO;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredientDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.repository.UserIngredientRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIngredientService {

    private final UserIngredientRepository userIngredientRepository;
    private final UserRepository userRepository;

    public List<UserIngredient> getUserIngredients(String userUuid) throws ObjectDoesNotExistInDatabaseException {
        User user = userRepository.findUserByUuid(userUuid).orElse(null);
        if (user != null) {
            return userIngredientRepository.getAllByOwner(user);
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "User with uuid: '" + userUuid + "' does not exist in database"
        );
    }

    public void addUserIngredient(UserIngredient userIngredient) {

        userIngredient.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));

        userIngredientRepository.save(userIngredient);
    }

    public void changeUserIngredient(ChangeUserIngredientDTO changeUserIngredientDTO)
            throws ObjectDoesNotExistInDatabaseException {
        UserIngredient userIngredient = userIngredientRepository
                .getUserIngredientByShortId(changeUserIngredientDTO.getShortId())
                .orElse(null);
        if (userIngredient != null) {
            if (changeUserIngredientDTO.getQuantity() != null) {
                userIngredient.setQuantity(changeUserIngredientDTO.getQuantity());
            }
            if (changeUserIngredientDTO.getExpirationDate() != null) {
                userIngredient.setExpirationDate(changeUserIngredientDTO.getExpirationDate());
            }
            userIngredientRepository.save(userIngredient);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "User ingredient with shortId: '" +
                        changeUserIngredientDTO.getShortId() +
                        "' does not exist in database"
        );
    }

    public void deleteUserIngredient(String shortId) {
        UserIngredient userIngredient = userIngredientRepository
                .getUserIngredientByShortId(shortId)
                .orElse(null);
        if (userIngredient != null) {
            userIngredientRepository.delete(userIngredient);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "User ingredient with shortId: '" + shortId + "' does not exist in database"
        );
    }

}
