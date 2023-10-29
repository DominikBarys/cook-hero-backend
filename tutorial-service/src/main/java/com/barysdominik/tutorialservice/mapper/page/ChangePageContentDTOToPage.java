package com.barysdominik.tutorialservice.mapper.page;

import com.barysdominik.tutorialservice.entity.page.ChangePageContentDTO;
import com.barysdominik.tutorialservice.entity.page.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChangePageContentDTOToPage {

//    public Page mapChangePageContentDTOToPage(ChangePageContentDTO changePageContentDTO) {
//        Page page = new Page();
//        page.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
//        page.setPageNumber(changePageContentDTO);
//    }

}
