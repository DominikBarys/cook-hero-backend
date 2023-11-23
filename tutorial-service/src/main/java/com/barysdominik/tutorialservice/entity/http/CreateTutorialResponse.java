package com.barysdominik.tutorialservice.entity.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
public class CreateTutorialResponse {
    private final String timestamp;
    private final String message;
    private final String tutorialShortId;

    public CreateTutorialResponse(String message, String tutorialShortId) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = message;
        this.tutorialShortId = tutorialShortId;
    }
}
