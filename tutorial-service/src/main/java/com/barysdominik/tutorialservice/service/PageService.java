package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.entity.page.Page;
import com.barysdominik.tutorialservice.entity.page.PageDTO;
import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.repository.PageRepository;
import com.barysdominik.tutorialservice.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final TutorialRepository tutorialRepository;


    public List<Page> getPages(String tutorialShortId) throws ObjectDoesNotExistInDatabaseException {
        Tutorial tutorial = tutorialRepository.findTutorialByShortId(tutorialShortId).orElse(null);
        if (tutorial != null) {
            return pageRepository.getAllByTutorial(tutorial);
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Any page with tutorial shortId: '" + tutorialShortId + "' does not exist in database"
        );
    }

    public Page getPage(String tutorialShortId, int pageNumber) throws ObjectDoesNotExistInDatabaseException {
        Tutorial tutorial = tutorialRepository.findTutorialByShortId(tutorialShortId).orElse(null);
        if (tutorial != null) {
            List<Page> pages = pageRepository.getPagesByTutorial(tutorial);
            for (Page page : pages) {
                if (page.getPageNumber() == pageNumber) {
                    return page;
                }
            }
            throw new ObjectDoesNotExistInDatabaseException(
                    "Page with number: '" + pageNumber + "' does not exist in database"
            );
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Tutorial with shortId: '" + tutorialShortId + "' does not exist in database"
        );
    }

    public long countPages(String tutorialShortId) throws ObjectDoesNotExistInDatabaseException {
        Tutorial tutorial = tutorialRepository.findTutorialByShortId(tutorialShortId).orElse(null);
        if (tutorial != null) {
            return pageRepository.countPagesByTutorial(tutorial);
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Tutorial with shortId: '" + tutorialShortId + "' does not exist in database"
        );
    }

    public void createPage(Page page) {
        page.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        pageRepository.save(page);
    }

    public void changePageHtmlContent(String shortId, String newHtmlContent)
            throws ObjectDoesNotExistInDatabaseException {
        Page page = pageRepository.findPageByShortId(shortId).orElse(null);
        if (page != null) {
            page.setHtmlContent(newHtmlContent);
            pageRepository.save(page);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Page with shortId: '" + shortId + "' does not exist in database"
        );
    }

    public void deletePage(String shortId) throws ObjectDoesNotExistInDatabaseException {
        Page page = pageRepository.findPageByShortId(shortId).orElse(null);
        if (page != null) {
            pageRepository.delete(page);
            return;
        }
        throw new ObjectDoesNotExistInDatabaseException(
                "Page with shortId: '" + shortId + "' does not exist in database"
        );
    }

}
