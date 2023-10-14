package com.barysdominik.auth.entity.http;

public enum Code {
    SUCCESS("Operation has ended with success");

    public final String label;

    Code(String label) {
        this.label = label;
    }
}
