package com.barysdominik.tutorialservice.entity.tutorial;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.dish.Dish;
import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tutorial")
public class Tutorial {
    //TODO przy tworzeniu poradnika trzeba zwiekszac userowi ilosc stworzonych poradnikow
    @Id
    @GeneratedValue(generator = "tutorial_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "tutorial_id_seq", sequenceName = "tutorial_id_seq", allocationSize = 1)
    private long id;
    private String shortId;
    private String name;
    @Column(nullable = false)
    private int timeToPrepare;
    @Column(nullable = false)
    private int difficulty;
    @Column(nullable = false)
    private LocalDate creationDate;//def
    @Column(nullable = false)
    private String[] imageUrls;
    @Column(nullable = false)
    private String shortDescription;
    private String parameters;
    private boolean hasMeat;
    private boolean isVeganRecipe;
    private boolean isSweetRecipe;
    private boolean isSpicyRecipe;
    @ManyToOne
    @JoinColumn(name = "dishId", referencedColumnName = "id")
    private Dish dish;
    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "tutorials_ingredients",
            joinColumns = @JoinColumn(name = "tutorial_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> mainIngredients;
    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;
    //dzieki temu moglem od razu dodac autora robiac tylko save na Tutorial
    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinColumn(name = "authorId", referencedColumnName = "id", nullable = false)
    private User author;
}
