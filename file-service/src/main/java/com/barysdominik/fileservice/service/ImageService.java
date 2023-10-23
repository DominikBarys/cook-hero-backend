package com.barysdominik.fileservice.service;

import com.barysdominik.fileservice.entity.image.Image;
import com.barysdominik.fileservice.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FtpService ftpService;

    public Image save(Image image) {
        return imageRepository.saveAndFlush(image);
    }

    public Image findByShortId(String shortId) {
        return imageRepository.findImageByShortId(shortId).orElse(null);
    }

    public void delete(Image image) {
        imageRepository.delete(image);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteUnusedImages() {
       List<Image> imagesList = imageRepository.findAllByUsedIsFalse().orElse(null);
       if(imagesList != null){
           for (Image image : imagesList) {
               try {
                   ftpService.deleteFile(image.getPath());
                   imageRepository.delete(image);
               } catch (IOException e) {
                   log.error("Cannot delete file with path: '" + image.getPath() + "'");
               }
           }
       }
    }
}
