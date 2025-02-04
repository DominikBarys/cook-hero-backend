package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.category.CategoryToCategoryDTO;
import com.barysdominik.tutorialservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor

public class CategoryMediator {

    private final CategoryService categoryService;
    private final CategoryToCategoryDTO categoryToCategoryDTO;

    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categoryService.getCategories().forEach(value -> {
            categoryDTOS.add(categoryToCategoryDTO.mapCategoryToCategoryDTO(value));
        });
        return ResponseEntity.ok(categoryDTOS);

    }

    public ResponseEntity<CategoryDTO> getCategory(String shortId) {
        Category category = categoryService.getCategory(shortId);
        if (category != null) {
            CategoryDTO categoryDTO = categoryToCategoryDTO.mapCategoryToCategoryDTO(category);
            return ResponseEntity.ok(categoryDTO);
        } else {
            throw new ObjectDoesNotExistInDatabaseException();
        }
    }

    public void createCategory(CategoryDTO categoryDTO) throws ObjectAlreadyExistInDatabaseException {
        categoryService.createCategory(categoryDTO);
    }

    public ResponseEntity<Response> deleteCategory(String shortId) {
        try {
            categoryService.deleteCategory(shortId);
            return ResponseEntity.ok(new Response("Category with shortId: '" + shortId + "' deleted successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Category with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }

}
