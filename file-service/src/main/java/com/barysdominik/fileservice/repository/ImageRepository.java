package com.barysdominik.fileservice.repository;

import com.barysdominik.fileservice.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findImageByShortId(String shortId);
    @Query(nativeQuery = true, value = "SELECT * FROM image where is_used = false")
    Optional<List<Image>> findAllByUsedIsFalse();
}
