package com.barysdominik.tutorialservice.mapper.tutorial;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialDTO;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TutorialToTutorialDTO {

    //TODO dodac tutaj to samo na wzor kategorii do disha autora i ingredientsow

    private final CategoryRepository categoryRepository;

    public TutorialDTO mapTutorialToTutorialDTO(Tutorial tutorial) {
        TutorialDTO tutorialDTO = new TutorialDTO();
        tutorialDTO.setShortId(tutorial.getShortId());
        tutorialDTO.setTimeToPrepare(tutorial.getTimeToPrepare());
        tutorialDTO.setDifficulty(tutorial.getDifficulty());
        tutorialDTO.setImageUrls(tutorial.getImageUrls());
        tutorialDTO.setShortDescription(tutorial.getShortDescription());
        tutorialDTO.setParameters(tutorial.getParameters());
        tutorialDTO.setDishId(0);
        tutorialDTO.setMainIngredientsIds(null);
        tutorialDTO.setCategoryDTO(createCategoryDTO(tutorial.getCategory()));
        tutorialDTO.setAuthorId(0);
        return tutorialDTO;
    }

    private CategoryDTO createCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setShortId(category.getShortId());
        return categoryDTO;
    }

}
