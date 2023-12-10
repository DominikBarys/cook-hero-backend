package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.dish.DishDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.dish.DishDTOToDish;
import com.barysdominik.tutorialservice.repository.DishRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final DishDTOToDish dishDTOToDish;
    private final TutorialRepository tutorialRepository;

    public List<Dish> getDishes() {
        return dishRepository.findAll();
    }

    public Dish getDish(String shortId) {
        return dishRepository.findDishByShortId(shortId).orElse(null);
    }

    public void createDish(DishDTO dishDTO) {
        dishRepository.findDishByName(dishDTO.getName()).ifPresent(value ->{
            throw new ObjectAlreadyExistInDatabaseException("Dish with this name already exist in database");
        });

        Dish dish = dishDTOToDish.mapDishDTOToDish(dishDTO);
        dish.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        dishRepository.save(dish);
    }

    public void deleteDish(String shortId) throws ObjectDoesNotExistInDatabaseException {
        Dish dish = dishRepository.findDishByShortId(shortId).orElse(null);
        if (dish != null) {
            List<Tutorial> tutorials = tutorialRepository.findTutorialsByDish(dish);
            for (Tutorial tutorial : tutorials) {
                tutorial.setDish(null);
                tutorialRepository.saveAll(tutorials);
            }
            dishRepository.delete(dish);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Dish with shortId: '" + shortId + "' does not exist in database"
        );
    }
}
