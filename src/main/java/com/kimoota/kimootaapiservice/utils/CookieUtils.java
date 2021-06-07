package com.kimoota.kimootaapiservice.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String key) {

        Optional<Cookie[]> cookies = Optional.ofNullable(request.getCookies());

        return cookies.isPresent()?
                Arrays.stream(cookies.get())
                        .filter(c -> c.getName().equals(key))
                        .findFirst():
                Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge) {

        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String key) {

        Optional.ofNullable(request.getCookies())
                .ifPresent(cookies ->
                        Arrays.stream(cookies)
                                .filter(c -> c.getName().equals(key))
                                .findFirst()
                                .ifPresent(c -> {
                                    c.setMaxAge(0);
                                    c.setPath("/");
                                    c.setValue("");
                                    response.addCookie(c);
                                }));
    }

}
