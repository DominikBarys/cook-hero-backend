package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.dish.DishDTO;
import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mediator.DishMediator;
import com.barysdominik.tutorialservice.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO dodac endpointy do gateweaya
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/dish")
public class DishController {
    private final DishMediator dishMediator;

    @GetMapping("/all")
    public ResponseEntity<List<DishDTO>> getDishes() {
        return dishMediator.getDishes();
    }

    @GetMapping
    public ResponseEntity<?> getDish(@RequestParam String shortId) {
        try {
            return dishMediator.getDish(shortId);
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Dish with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createDish(@RequestBody DishDTO dishDTO) {
        try {
            dishMediator.createDish(dishDTO);
        } catch (ObjectAlreadyExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Dish with this name already exist")
            );
        }
        return ResponseEntity.ok(new Response("Operation ended with success"));
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteDish(String shortId) {
        return dishMediator.deleteDish(shortId);
    }

}
