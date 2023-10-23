package com.barysdominik.fileservice.facade;

import com.barysdominik.fileservice.entity.http.Response;
import com.barysdominik.fileservice.mediator.ImageMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private final ImageMediator imageMediator;

    @PostMapping
    public ResponseEntity<?> saveImage(@RequestParam MultipartFile multipartFile) {
        return imageMediator.saveImage(multipartFile);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteImage(@RequestParam String shortId) {
        return imageMediator.deleteImage(shortId);
    }

    @GetMapping
    public ResponseEntity<?> getImage(@RequestParam String shortId) throws IOException {
        return imageMediator.getImage(shortId);
    }

    @PatchMapping
    public ResponseEntity<Response> activateImage(@RequestParam String shortId) {
        return imageMediator.activateImage(shortId);
    }

}
