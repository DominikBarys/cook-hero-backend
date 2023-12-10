package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.category.CategoryDTOToCategory;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDTOToCategory categoryDTOToCategory;
    private final TutorialRepository tutorialRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(String shortId) {
        return categoryRepository.findCategoryByShortId(shortId).orElse(null);
    }

    public void createCategory(CategoryDTO categoryDTO) {
        categoryRepository.findCategoryByName(categoryDTO.getName()).ifPresent(value -> {
                    throw new ObjectAlreadyExistInDatabaseException("Category with this name already exist");
                }
        );

        Category category = categoryDTOToCategory.mapCategoryDTOToCategory(categoryDTO);
        category.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        categoryRepository.save(category);
    }

    public void deleteCategory(String shortId) throws ObjectDoesNotExistInDatabaseException {
        Category category = categoryRepository.findCategoryByShortId(shortId).orElse(null);
        if (category != null) {
            List<Tutorial> tutorials = tutorialRepository.findTutorialsByCategory(category);
            for (Tutorial tutorial : tutorials) {
                tutorial.setCategory(null);
                tutorialRepository.saveAll(tutorials);
            }
            categoryRepository.delete(category);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Category with shortId: '" + shortId + "' does not exist in database"
        );
    }
}
