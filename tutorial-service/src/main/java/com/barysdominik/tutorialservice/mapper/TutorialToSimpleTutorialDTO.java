package com.barysdominik.tutorialservice.mapper;

import com.barysdominik.tutorialservice.entity.tutorial.SimpleTutorialDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TutorialToSimpleTutorialDTO {

    public SimpleTutorialDTO mapTutorialToSimpleTutorialDTO(Tutorial tutorial) {
        SimpleTutorialDTO simpleTutorialDTO = new SimpleTutorialDTO();
        simpleTutorialDTO.setName(tutorial.getName());
        simpleTutorialDTO.setTimeToPrepare(tutorial.getTimeToPrepare());
        simpleTutorialDTO.setDifficulty(tutorial.getDifficulty());
        simpleTutorialDTO.setImageUrl(getFirstImage(tutorial));
        simpleTutorialDTO.setShortDescription(tutorial.getShortDescription());
        simpleTutorialDTO.setHasMeat(tutorial.isHasMeat());
        simpleTutorialDTO.setVeganRecipe(tutorial.isVeganRecipe());
        simpleTutorialDTO.setSweetRecipe(tutorial.isSweetRecipe());
        simpleTutorialDTO.setSpicyRecipe(tutorial.isSpicyRecipe());
        return simpleTutorialDTO;
    }

    private String getFirstImage(Tutorial tutorial) {
        String[] imageUrls = tutorial.getImageUrls();
        return (imageUrls != null && imageUrls.length >=1) ? imageUrls[0] : null;
    }

}
