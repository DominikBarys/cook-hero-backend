package com.barysdominik.auth.entity.http;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class LoginResponse {
    private final String timestamp;
    private final boolean message;
    private final Code code;

    public LoginResponse(boolean message) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = message;
        this.code = Code.SUCCESS;
    }
}
