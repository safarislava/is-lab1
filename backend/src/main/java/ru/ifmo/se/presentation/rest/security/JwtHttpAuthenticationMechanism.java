package ru.ifmo.se.presentation.rest.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ifmo.se.application.service.JwtService;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class JwtHttpAuthenticationMechanism implements HttpAuthenticationMechanism {
    private static final String BEARER_PREFIX = "Bearer ";

    @Inject
    private JwtService jwtService;

    @Inject
    private CookieService cookieService;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request,
                                               HttpServletResponse response,
                                               HttpMessageContext httpMessageContext) {
        Optional<String> tokenOpt = extractToken(request);

        if (tokenOpt.isPresent()) {
            Optional<DecodedJWT> decodedOpt = jwtService.verifyToken(tokenOpt.get());
            if (decodedOpt.isPresent()) {
                DecodedJWT jwt = decodedOpt.get();
                String username = jwtService.extractUsername(jwt);
                Set<String> roles = jwtService.extractRoles(jwt);

                return httpMessageContext.notifyContainerAboutLogin(
                        new CallerPrincipal(username),
                        roles
                );
            }
        }

        if (httpMessageContext.isProtected()) {
            return httpMessageContext.responseUnauthorized();
        }

        return httpMessageContext.doNothing();
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        Optional<String> cookieToken = cookieService.extractCookie(
                request,
                CookieService.ACCESS_TOKEN_COOKIE_NAME
        );
        if (cookieToken.isPresent()) {
            return cookieToken;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String bearerToken = authHeader.substring(BEARER_PREFIX.length()).trim();
            if (!bearerToken.isBlank()) {
                return Optional.of(bearerToken);
            }
        }

        return Optional.empty();
    }
}
