package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import com.barysdominik.tutorialservice.repository.DishRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public long countSearchedResults(
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
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Tutorial> root = query.from(Tutorial.class);
        List<Predicate> predicates = prepareQuery(
                name,
                dishUuid,
                categoryUuid,
                authorUuid,
                hasMeat,
                isVeganRecipe,
                isSweetRecipe,
                isSpicyRecipe,
                criteriaBuilder,
                root
        );
        query.select(criteriaBuilder.count(root)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getSingleResult();
    }

    public List<Tutorial> getTutorial(
            int page,
            int limit,
            String sort,
            String order,
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

        if (uuid != null) {
            return tutorialRepository.findTutorialByUuid(uuid).stream().toList();
        }

        if (page <= 0) {
            page = 1;
        }

        List<Predicate> predicates = prepareQuery(
                name,
                dishUuid,
                categoryUuid,
                authorUuid,
                hasMeat,
                isVeganRecipe,
                isSweetRecipe,
                isSpicyRecipe,
                criteriaBuilder,
                root
        );

        if (!sort.isEmpty() && !order.isEmpty()) {
            String column = switch (sort) {
                case "timeToPrepare" -> "timeToPrepare";
                case "difficulty" -> "difficulty";
                default -> "creationDate";
            };
            Order orderQuery;
            if(order.equals("desc")) {
                orderQuery = criteriaBuilder.desc(root.get(column));
            } else {
                orderQuery = criteriaBuilder.asc(root.get(column));
            }
            query.orderBy(orderQuery);
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }

    public List<Predicate> prepareQuery(
            String name,
            String dishUuid,
            String categoryUuid,
            String authorUuid,
            boolean hasMeat,
            boolean isVeganRecipe,
            boolean isSweetRecipe,
            boolean isSpicyRecipe,
            CriteriaBuilder criteriaBuilder,
            Root<Tutorial> root
    ) {
        List<Predicate> predicates = new ArrayList<>();

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

        if (hasMeat) {
            predicates.add(criteriaBuilder.isTrue(root.get("hasMeat")));
        }

        if (isVeganRecipe) {
            predicates.add(criteriaBuilder.isTrue(root.get("isVeganRecipe")));
        }

        if (isSweetRecipe) {
            predicates.add(criteriaBuilder.isTrue(root.get("isSweetRecipe")));
        }

        if (isSpicyRecipe) {
            predicates.add(criteriaBuilder.isTrue(root.get("isSpicyRecipe")));
        }

        return predicates;
    }

    public long countTutorials() {
        return tutorialRepository.count();
    }
}
