package com.barysdominik.tutorialservice.mapper;

import com.barysdominik.tutorialservice.entity.category.Category;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.entity.tutorial.TutorialFormDTO;
import com.barysdominik.tutorialservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TutorialFormDTOToTutorial {
    private final CategoryRepository categoryRepository;

    public Tutorial mapTutorialFormDTOToTutorial(TutorialFormDTO tutorialFormDTO) {
        Tutorial tutorial = new Tutorial();
        tutorial.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12)); //niepotrzebne
        tutorial.setName(tutorialFormDTO.getName());
        tutorial.setTimeToPrepare(tutorialFormDTO.getTimeToPrepare());
        tutorial.setDifficulty(tutorialFormDTO.getDifficulty());
        tutorial.setCreationDate(LocalDate.now()); //niepotrzebne
        tutorial.setImageUrls(tutorialFormDTO.getImagesUuid());
        tutorial.setShortDescription(tutorialFormDTO.getShortDescription());
        tutorial.setParameters(tutorialFormDTO.getParameters());
        tutorial.setHasMeat(tutorialFormDTO.isHasMeat());
        tutorial.setVeganRecipe(tutorialFormDTO.isVeganRecipe());
        tutorial.setSweetRecipe(tutorialFormDTO.isSweetRecipe());
        tutorial.setSpicyRecipe(tutorialFormDTO.isSpicyRecipe());

        Category category = categoryRepository.findCategoryByShortId(tutorialFormDTO.getCategoryShortId()).orElse(null);
        tutorial.setCategory(category);

        return tutorial;
    }

}
