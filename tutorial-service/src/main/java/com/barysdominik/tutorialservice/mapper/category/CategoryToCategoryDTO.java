package com.barysdominik.tutorialservice.mapper.category;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDTO {

    public CategoryDTO mapCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setShortId(category.getShortId());
        return categoryDTO;
    }


}
