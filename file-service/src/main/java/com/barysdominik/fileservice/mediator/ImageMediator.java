package com.barysdominik.fileservice.mediator;

import com.barysdominik.fileservice.entity.http.Response;
import com.barysdominik.fileservice.entity.image.Image;
import com.barysdominik.fileservice.entity.image.ImageDTO;
import com.barysdominik.fileservice.exception.FtpException;
import com.barysdominik.fileservice.service.FtpService;
import com.barysdominik.fileservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageMediator {
    private final ImageService imageService;
    private final FtpService ftpService;

    public ResponseEntity<?> saveImage(MultipartFile multipartFile) {
        try {
            if(multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1).equals("png")) {
                Image image = ftpService.uploadFileToFtp(multipartFile);
                image = imageService.save(image);
                imageService.save(image);
                return ResponseEntity.ok(
                        ImageDTO.builder()
                                .shortId(image.getShortId())
                                .createdAt(image.getCreatedAt())
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Media type different than '.png' are not supported"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("File does not exist"));
        } catch (FtpException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("A problem has occurred while saving file to ftp"));
        }
    }

    public ResponseEntity<Response> deleteImage(String shortId) {
        try {
            Image image = imageService.findByShortId(shortId);
            if(image != null) {
                imageService.delete(image);
                if(ftpService.deleteFile(image.getPath())) {
                    return ResponseEntity.ok(new Response("File deleted successfully"));
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new Response("An unexpected error has occurred while deleting file from ftp server"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("File does not exist in ftp server"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An unexpected error has occurred while deleting file from ftp server"));
        }
    }

    public ResponseEntity<?> getImage(String shortId) throws IOException {
        Image image = imageService.findByShortId(shortId);
        if(image != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(ftpService.getFile(image).toByteArray(), httpHeaders, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("File does not exist"));
    }

    public ResponseEntity<Response> activateImage(String shortId) {
        Image image = imageService.findByShortId(shortId);
        if(image != null) {
            image.setUsed(true);
            imageService.save(image);
            return ResponseEntity.ok(new Response("Image has been activated successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Image does not exist"));
    }

}
