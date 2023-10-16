package com.barysdominik.auth.entity.http;

public enum Code {
    //polish names because this will be displayed in front end
    SUCCESS("Operacja zakończyła się pomyślnie"),
    PERMIT("Dostęp został przyznany"),
    CODE1("Logowanie nie powiodło się"),
    CODE2("Użytkownik o podanej nazwie nie istnieje"),
    CODE3("Wskazany token jest pusty lub nie ważny");

    public final String label;

    Code(String label) {
        this.label = label;
    }
}
