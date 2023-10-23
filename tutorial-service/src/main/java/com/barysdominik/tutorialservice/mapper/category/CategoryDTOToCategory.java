package com.barysdominik.tutorialservice.mapper.category;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTOToCategory {

    public Category mapCategoryDTOToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setShortId(category.getShortId());
        return category;
    }

}
