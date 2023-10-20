package com.barysdominik.tutorialservice.entity.tutorial;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TutorialDTO {
    private long id;
    private BigDecimal price;
    private int timeToPrepare;
    private int difficulty;
    private LocalDate creationDate;
    private List<String> imageUrls;
    private String shortDescription;
    private String parameters;
    private int rating;
    private long dishId;
    private List<Long> mainIngredientsIds;
    private long categoryId;
    private long authorId;
}
