package com.barysdominik.fileservice.entity.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ImageDTO {
    private String shortId;
    private LocalDate createdAt;
}
