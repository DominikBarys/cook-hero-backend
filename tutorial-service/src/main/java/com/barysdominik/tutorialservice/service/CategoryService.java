package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.category.CategoryDTOToCategory;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDTOToCategory categoryDTOToCategory;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(String shortId) {
        return categoryRepository.findCategoryByShortId(shortId).orElse(null);
    }

    public void createCategory(CategoryDTO categoryDTO) {
        Category category = categoryDTOToCategory.mapCategoryDTOToCategory(categoryDTO);
        category.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));

        categoryRepository.findCategoryByName(category.getName()).ifPresent(value -> {
                    throw new ObjectAlreadyExistInDatabaseException("Category with this name already exist");
                }
        );

        categoryRepository.save(category);
    }

    public Optional<Category> getCategories(String shortId) {
        return categoryRepository.findCategoryByShortId(shortId);
    }
}
