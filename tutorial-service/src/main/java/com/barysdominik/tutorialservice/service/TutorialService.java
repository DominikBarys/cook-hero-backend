package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import com.barysdominik.tutorialservice.repository.DishRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorialService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final TutorialRepository tutorialRepository;
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    //na ich podstawie bedzie potem sortowanie
//    Integer timeToPrepare,
//    Integer difficulty,
//    LocalDate creationDate,
//    Integer rating,

    //name, price_min, price_max, timeToPrepare, difficulty, creationDate
    //rating, dish, ingredients, category, author, uuid
    //ZAKLADAMY ZE JAK PRZYCISK NIE JEST WCISNIETY TO BOOLEANY SA FALSE
    public List<Tutorial> getTutorial(
            String uuid,
            String name,
            String dishUuid,
            String categoryUuid,
            String authorUuid,
            boolean hasMeat,
            boolean isVeganRecipe,
            boolean isSweetRecipe,
            boolean isSpicyRecipe
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tutorial> query = criteriaBuilder.createQuery(Tutorial.class);
        Root<Tutorial> root = query.from(Tutorial.class);
        List<Predicate> predicates = new ArrayList<>();

        if (uuid != null) {
            return tutorialRepository.findTutorialByUuid(uuid).stream().toList();
        }

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (dishUuid != null && !dishUuid.trim().isEmpty()) {
            dishRepository.findDishByUuid(dishUuid).ifPresent(
                    value -> predicates.add(criteriaBuilder.equal(root.get("dish"), value))
            );
        }

        if (categoryUuid != null && !categoryUuid.trim().isEmpty()) {
            categoryRepository.findCategoryByUuid(categoryUuid).ifPresent(
                    value -> predicates.add(criteriaBuilder.equal(root.get("category"), value))
            );
        }

        if (authorUuid != null && !authorUuid.trim().isEmpty()) {
            userRepository.findUserByUuid(authorUuid).ifPresent(
                    value -> predicates.add(criteriaBuilder.equal(root.get("author"), value))
            );
        }

        if(hasMeat) {
            predicates.add(criteriaBuilder.isTrue(root.get("hasMeat")));
        }

        if(isVeganRecipe) {
            predicates.add(criteriaBuilder.isTrue(root.get("isVeganRecipe")));
        }

        if(isSweetRecipe) {
            predicates.add(criteriaBuilder.isTrue(root.get("isSweetRecipe")));
        }

        if(isSpicyRecipe) {
            predicates.add(criteriaBuilder.isTrue(root.get("isSpicyRecipe")));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    public long countTutorials() {
        return tutorialRepository.count();
    }
}
