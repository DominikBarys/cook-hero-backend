package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.CategoryToCategoryDTO;
import com.barysdominik.tutorialservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor

public class CategoryMediator {

    private final CategoryService categoryService;
    private final CategoryToCategoryDTO categoryToCategoryDTO;

    public ResponseEntity<List<CategoryDTO>> getCategory() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categoryService.getCategory().forEach(value->{
            categoryDTOS.add(categoryToCategoryDTO.mapCategoryToCategoryDTO(value));
        });
        return ResponseEntity.ok(categoryDTOS);

    }

    public void createCategory(CategoryDTO categoryDTO) throws ObjectAlreadyExistInDatabaseException {
        categoryService.createCategory(categoryDTO);
    }

}
