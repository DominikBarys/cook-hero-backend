package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.userIngredient.ChangeUserIngredientDTO;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredient;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredientDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.useringredient.UserIngredientDTOToUserIngredient;
import com.barysdominik.tutorialservice.mapper.useringredient.UserIngredientToUserIngredientDTO;
import com.barysdominik.tutorialservice.service.UserIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserIngredientMediator {
    private final UserIngredientService userIngredientService;
    private final UserIngredientToUserIngredientDTO userIngredientToUserIngredientDTO;
    private final UserIngredientDTOToUserIngredient userIngredientDTOToUserIngredient;

    public ResponseEntity<List<UserIngredientDTO>> getUserIngredients(String userUuid) throws ObjectDoesNotExistInDatabaseException {
        List<UserIngredient> userIngredients = userIngredientService.getUserIngredients(userUuid);
        List<UserIngredientDTO> userIngredientDTOS = new ArrayList<>();
        for (UserIngredient userIngredient : userIngredients) {
            userIngredientDTOS.add(
                    userIngredientToUserIngredientDTO.mapUserIngredientToUserIngredientDTO(userIngredient)
            );
        }
        return ResponseEntity.ok(userIngredientDTOS);
    }

    public ResponseEntity<Response> addUserIngredients(UserIngredientDTO userIngredientDTO) {
        try {

            UserIngredient userIngredient =
                    userIngredientDTOToUserIngredient.mapUserIngredientDTOToUserIngredient(userIngredientDTO);

            userIngredientService.addUserIngredient(userIngredient);
            return ResponseEntity.ok(new Response("User ingredient created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("An unexpected error has occurred when adding user ingredient"));
        }
    }

    public ResponseEntity<Response> changeUserIngredient(ChangeUserIngredientDTO changeUserIngredientDTO) {
        try {
            userIngredientService.changeUserIngredient(changeUserIngredientDTO);
            return ResponseEntity.ok(new Response("User ingredient changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing user ingredient"));
        }
    }

    public ResponseEntity<Response> deleteUserIngredient(String shortId) {
        try {
            userIngredientService.deleteUserIngredient(shortId);
            return ResponseEntity.ok(new Response("User ingredient deleted successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            throw new ObjectDoesNotExistInDatabaseException(
                    "User ingredient with shortId: '" + shortId + "' does not exist in database"
            );
        }
    }

}
