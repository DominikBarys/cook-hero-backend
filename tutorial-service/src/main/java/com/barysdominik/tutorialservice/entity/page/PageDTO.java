package com.barysdominik.tutorialservice.entity.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private String shortId;
    private int pageNumber;
    private String htmlContent;
    private String tutorialShortId;
}
