package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.dish.DishDTO;
import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.dish.DishToDishDTO;
import com.barysdominik.tutorialservice.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DishMediator {

    private final DishService dishService;
    private final DishToDishDTO dishToDishDTO;

    public ResponseEntity<List<DishDTO>> getDishes() {
        List<DishDTO> dishDTOS = new ArrayList<>();
        dishService.getDishes().forEach(value ->{
            dishDTOS.add(dishToDishDTO.mapDishToDishDTO(value));
        });
        return ResponseEntity.ok(dishDTOS);
    }

    public ResponseEntity<DishDTO> getDish(String shortId) {
        Dish dish = dishService.getDish(shortId);
        if(dish != null) {
            DishDTO dishDTO = dishToDishDTO.mapDishToDishDTO(dish);
            return ResponseEntity.ok(dishDTO);
        } else {
            throw new ObjectDoesNotExistInDatabaseException();
        }
    }

    public void createDish(DishDTO dishDTO) throws ObjectAlreadyExistInDatabaseException {
        dishService.createDish(dishDTO);
    }

    public ResponseEntity<Response> deleteDish(String shortId) {
        try {
            dishService.deleteDish(shortId);
            return ResponseEntity.ok(new Response("Dish with shortId: '" + shortId + "' deleted successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Dish with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }
    
}
