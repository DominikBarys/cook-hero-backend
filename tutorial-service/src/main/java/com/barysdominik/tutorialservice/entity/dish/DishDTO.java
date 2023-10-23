package com.barysdominik.tutorialservice.entity.dish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    private String name;
    private String shortId;
}
