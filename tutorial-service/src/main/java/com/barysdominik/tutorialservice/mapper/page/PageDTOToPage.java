package com.barysdominik.tutorialservice.mapper.page;

import com.barysdominik.tutorialservice.entity.page.Page;
import com.barysdominik.tutorialservice.entity.page.PageDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageDTOToPage {

    private final TutorialRepository tutorialRepository;

    public Page mapPageDTOToPage(PageDTO pageDTO) {
        Page page = new Page();
        page.setShortId(pageDTO.getShortId());
        page.setPageNumber(pageDTO.getPageNumber());
        page.setHtmlContent(pageDTO.getHtmlContent());

        Tutorial tutorial = tutorialRepository.findTutorialByShortId(pageDTO.getTutorialShortId()).orElse(null);
        page.setTutorial(tutorial);

        return page;
    }
}
