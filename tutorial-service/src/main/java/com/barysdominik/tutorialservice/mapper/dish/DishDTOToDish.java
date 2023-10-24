package com.barysdominik.tutorialservice.mapper.dish;

import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.dish.DishDTO;
import org.springframework.stereotype.Component;

@Component
public class DishDTOToDish {

    public Dish mapDishDTOToDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setShortId(dishDTO.getShortId());
        return dish;
    }

}
