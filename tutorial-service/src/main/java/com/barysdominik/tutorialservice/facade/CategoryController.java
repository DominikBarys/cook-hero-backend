package com.barysdominik.tutorialservice.facade;

import com.barysdominik.tutorialservice.entity.category.CategoryDTO;
import com.barysdominik.tutorialservice.entity.http.Response;
import com.barysdominik.tutorialservice.exception.ObjectAlreadyExistInDatabaseException;
import com.barysdominik.tutorialservice.exception.ObjectDoesNotExistInDatabaseException;
import com.barysdominik.tutorialservice.mediator.CategoryMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/category")
public class CategoryController {
    private final CategoryMediator categoryMediator;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return categoryMediator.getCategories();
    }

    @GetMapping
    public ResponseEntity<?> getCategory(@RequestParam String shortId) {
        try {
            return categoryMediator.getCategory(shortId);
        } catch (ObjectDoesNotExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Category with shortId: '" + shortId + "' does not exist in database")
            );
        }
    }

    //TODO poprawic rzucanie wyjatkow na podstawie na przyklad tego
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            categoryMediator.createCategory(categoryDTO);
        } catch (ObjectAlreadyExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("Category with this name already exist")
            );
        }
        return ResponseEntity.ok(new Response("Operation ended with success"));
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteCategory(String shortId){
        return categoryMediator.deleteCategory(shortId);
    }

}
