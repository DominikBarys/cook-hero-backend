package com.barysdominik.auth.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CookieService {

    public Cookie generateCookie(String name, String value, int exp) {
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setMaxAge(exp);
        cookie.setHttpOnly(true);
        return cookie;

    }

    public Cookie removeCookie(List<Cookie> cookieList, String name) {
        for (Cookie cookie : cookieList) {
            if(cookie.getName().equals(name)) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setHttpOnly(true);
                return cookie;
            }
        }
        return null;
    }

}
