package com.barysdominik.tutorialservice.mapper.dish;

import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.dish.DishDTO;

public class DishToDishDTO {

    public DishDTO mapDishToDishDTO(Dish dish) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName(dish.getName());
        dishDTO.setShortId(dish.getShortId());
        return dishDTO;
    }

}
