package com.barysdominik.tutorialservice.entity.ingredient;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(generator = "ingredient_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ingredient_id_seq", sequenceName = "ingredient_id_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String name;
    private String shortId;
    @ManyToMany(mappedBy = "mainIngredients")
    private List<Tutorial> tutorials;
}
