package com.barysdominik.tutorialservice.mapper.page;

import com.barysdominik.tutorialservice.entity.page.Page;
import com.barysdominik.tutorialservice.entity.page.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageToPageDTO {
    public PageDTO mapPageToPageDTO(Page page) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setShortId(page.getShortId());
        pageDTO.setPageNumber(page.getPageNumber());
        pageDTO.setHtmlContent(page.getHtmlContent());
        pageDTO.setTutorialShortId(page.getTutorial().getShortId());
        return pageDTO;
    }
}
