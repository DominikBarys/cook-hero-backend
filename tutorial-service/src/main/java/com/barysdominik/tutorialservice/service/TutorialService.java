package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import com.barysdominik.tutorialservice.repository.DishRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import com.barysdominik.tutorialservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TutorialService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final TutorialRepository tutorialRepository;
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    @Value("${external.url.file-service}")
    private String FILE_SERVICE_EXTERNAL_URL;

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
            String dishShortId,
            String categoryShortId,
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
                dishShortId,
                categoryShortId,
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
            String shortId,
            String name,
            String dishShortId,
            String categoryShortId,
            String authorUuid,
            boolean hasMeat,
            boolean isVeganRecipe,
            boolean isSweetRecipe,
            boolean isSpicyRecipe
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tutorial> query = criteriaBuilder.createQuery(Tutorial.class);
        Root<Tutorial> root = query.from(Tutorial.class);

        if (shortId != null) {
            return tutorialRepository.findTutorialByShortId(shortId).stream().toList();
        }

        if (page <= 0) {
            page = 1;
        }

        List<Predicate> predicates = prepareQuery(
                name,
                dishShortId,
                categoryShortId,
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
            if (order.equals("desc")) {
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
            String dishShortId,
            String categoryShortId,
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

        if (dishShortId != null && !dishShortId.trim().isEmpty()) {
            dishRepository.findDishByShortId(dishShortId).ifPresent(
                    value -> predicates.add(criteriaBuilder.equal(root.get("dish"), value))
            );
        }

        if (categoryShortId != null && !categoryShortId.trim().isEmpty()) {
            categoryRepository.findCategoryByShortId(categoryShortId).ifPresent(
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

    @Transactional
    public void createTutorial(Tutorial tutorial) {
        tutorial.setCreationDate(LocalDate.now());
        tutorial.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        tutorialRepository.save(tutorial);

        for (String shortId : tutorial.getImageUrls()) {
            activateImage(shortId);
        }
    }

    public void activateImage(String shortId) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(FILE_SERVICE_EXTERNAL_URL + "?shortId=" + shortId))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("An unexpected error has occurred");
        }
    }

    public void delete(String shortId) {
        tutorialRepository.findTutorialByShortId(shortId).ifPresentOrElse(
                value -> {
                    tutorialRepository.save(value);
                    for(String imageUrl : value.getImageUrls()) {
                        deleteImage(imageUrl);
                    }
                },
                () -> {
                    throw new RuntimeException("An unexpected error has occurred");
                }
        );
    }

    private void deleteImage(String shortId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(FILE_SERVICE_EXTERNAL_URL + "?shortId=" + shortId);
    }

    public long countTutorials() {
        return tutorialRepository.count();
    }
}
