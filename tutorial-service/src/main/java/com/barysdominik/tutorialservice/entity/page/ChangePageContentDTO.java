package com.barysdominik.tutorialservice.entity.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePageContentDTO {
    private String shortId;
    private String newHtmlContent;
}
