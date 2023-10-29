package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.entity.page.ChangePageContentDTO;
import com.barysdominik.tutorialservice.entity.page.PageDTO;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mediator.PageMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page")
public class PageController {

    private final PageMediator pageMediator;

    @GetMapping
    public ResponseEntity<?> getPage(@RequestParam String tutorialShortId, @RequestParam int pageNumber) {
        try {
            return pageMediator.getPage(tutorialShortId, pageNumber);
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error has occurred when getting a page");
        }
    }

    @PostMapping
    public ResponseEntity<Response> createPage(@RequestBody PageDTO pageDTO) {
        return pageMediator.createPage(pageDTO);
    }

    @PatchMapping
    public ResponseEntity<Response> changePageHtmlContent(@RequestBody ChangePageContentDTO changePageContentDTO) {
        return pageMediator.changePageHtmlContent(changePageContentDTO);
    }

    @DeleteMapping
    public ResponseEntity<Response> deletePage(@RequestParam String shortId) {
        return pageMediator.deletePage(shortId);
    }

}
