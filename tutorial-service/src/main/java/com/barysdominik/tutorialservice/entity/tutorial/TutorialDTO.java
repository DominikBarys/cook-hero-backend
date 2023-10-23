package com.barysdominik.tutorialservice.entity.tutorial;

import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorialDTO {
    //TODO zmienic to dish, mainIngredients i authorId na wzor kategorii
    private String shortId;
    private int timeToPrepare;
    private int difficulty;
    private LocalDate creationDate;
    private String[] imageUrls;
    private String shortDescription;
    private String parameters;
    private long dishId;
    private List<Long> mainIngredientsIds;
    private CategoryDTO categoryDTO;
    private long authorId;
}
