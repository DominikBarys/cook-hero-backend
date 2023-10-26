package com.barysdominik.tutorialservice.entity.userIngredient;

import com.barysdominik.tutorialservice.entity.ingredient.Ingredient;
import com.barysdominik.tutorialservice.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_ingredients")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserIngredient {
    @Id
    @GeneratedValue(generator = "user_ingredients_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_ingredients_id_seq", sequenceName = "user_ingredients_id_seq", allocationSize = 1)
    private long id;
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "ingredientId", referencedColumnName = "id")
    private Ingredient ingredientId;
    private LocalDate expirationDate;
    @Column(nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User owner;

}
