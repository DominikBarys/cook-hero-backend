package com.barysdominik.tutorialservice.mediator;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.page.Page;
import com.barysdominik.tutorialservice.entity.page.PageDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mapper.page.PageDTOToPage;
import com.barysdominik.tutorialservice.mapper.page.PageToPageDTO;
import com.barysdominik.tutorialservice.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageMediator {

    private final PageService pageService;
    private final PageToPageDTO pageToPageDTO;
    private final PageDTOToPage pageDTOToPage;

    public ResponseEntity<PageDTO> getPage(String tutorialShortId, int pageNumber)
            throws ObjectDoesNotExistInDatabaseException{
        Page page = pageService.getPage(tutorialShortId, pageNumber);
        PageDTO pageDTO = pageToPageDTO.mapPageToPageDTO(page);
        long totalCount = pageService.countPages(tutorialShortId);

        return ResponseEntity.ok().header(
                "X-Total-Count",
                String.valueOf(totalCount)).body(pageDTO);
    }

    public ResponseEntity<Response> createPage(PageDTO pageDTO) {
        try {
            Page page = pageDTOToPage.mapPageDTOToPage(pageDTO);
            pageService.createPage(page);
            return ResponseEntity.ok(new Response("Page created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("An unexpected error has occurred when creating page"));
        }
    }

    public ResponseEntity<Response> changePageHtmlContent(String shortId, String newHtmlContent) {
        try {
            pageService.changePageHtmlContent(shortId, newHtmlContent);
            return ResponseEntity.ok(new Response("Html content changed successfully"));
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Something went wrong while changing html content"));
        }
    }

}
