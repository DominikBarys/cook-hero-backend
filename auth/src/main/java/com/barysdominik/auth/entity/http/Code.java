package com.barysdominik.auth.entity.http;

public enum Code {
    //polish names because this will be displayed in front end
    SUCCESS("Operacja zakończyła się pomyślnie"),
    PERMIT("Dostęp został przyznany"),
    LOGIN_FAILED("Logowanie nie powiodło się"),
    USER_NOT_FOUND("Użytkownik o podanej nazwie nie istnieje"),
    INVALID_TOKEN("Wskazany token jest pusty lub nie ważny"),
    DUPLICATE_USERNAME("Użytkownik o tej nazwie już istnieje"),
    DUPLICATE_EMAIL("Użytkownik o tey mailu już istnieje");

    public final String label;

    Code(String label) {
        this.label = label;
    }
}
