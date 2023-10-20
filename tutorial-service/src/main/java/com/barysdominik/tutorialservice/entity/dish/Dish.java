package com.barysdominik.tutorialservice.entity.dish;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dish")
public class Dish {
    //TODO uuid
    @Id
    @GeneratedValue(generator = "dish_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "dish_id_seq", sequenceName = "dish_id_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String name;
}
