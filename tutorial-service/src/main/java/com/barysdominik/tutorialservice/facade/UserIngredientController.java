package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.userIngredient.ChangeUserIngredientDTO;
import com.barysdominik.tutorialservice.entity.userIngredient.UserIngredientDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mediator.UserIngredientMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-ingredient")
public class UserIngredientController {
    private final UserIngredientMediator userIngredientMediator;

    @GetMapping
    public ResponseEntity<?> getUserIngredients(@RequestParam String userUuid) {
        try {
            return userIngredientMediator.getUserIngredients(userUuid);
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error has occurred when getting user ingredients");
        }
    }

    @PostMapping
    public ResponseEntity<Response> addUserIngredients(@RequestBody UserIngredientDTO userIngredientDTO) {
        return userIngredientMediator.addUserIngredients(userIngredientDTO);
    }

    @PatchMapping
    public ResponseEntity<Response> changeUserIngredient(@RequestBody ChangeUserIngredientDTO changeUserIngredientDTO) {
        return userIngredientMediator.changeUserIngredient(changeUserIngredientDTO);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteUserIngredient(@RequestParam String shortId) {
        return userIngredientMediator.deleteUserIngredient(shortId);
    }


}
