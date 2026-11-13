package ru.ifmo.se.presentation.rest.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.NewCookie;

import java.util.Arrays;
import java.util.Optional;

@ApplicationScoped
public class CookieService {
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private static final String ROOT_PATH = "/";
    private static final String AUTH_PATH = "/api/auth";

    private static final int ACCESS_TOKEN_MAX_AGE = 15 * 60;
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;

    public NewCookie createAccessTokenCookie(String token) {
        return new NewCookie.Builder(ACCESS_TOKEN_COOKIE_NAME)
                .value(token)
                .path(ROOT_PATH)
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .httpOnly(true)
                .secure(false)
                .sameSite(NewCookie.SameSite.LAX)
                .build();
    }

    public NewCookie createRefreshTokenCookie(String token) {
        return new NewCookie.Builder(REFRESH_TOKEN_COOKIE_NAME)
                .value(token)
                .path(AUTH_PATH)
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .httpOnly(true)
                .secure(false)
                .sameSite(NewCookie.SameSite.LAX)
                .build();
    }

    public NewCookie createExpiredCookie(String name, String path) {
        return new NewCookie.Builder(name)
                .value("")
                .path(path)
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .sameSite(NewCookie.SameSite.LAX)
                .build();
    }

    public Optional<String> extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
