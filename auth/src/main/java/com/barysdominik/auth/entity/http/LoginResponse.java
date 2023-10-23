package com.barysdominik.auth.entity.http;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class LoginResponse {
    private final String timestamp;
    private final boolean isLoggedIn;
    private final Code code;

    public LoginResponse(boolean message, Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.isLoggedIn = message;
        this.code = code;
    }
}
