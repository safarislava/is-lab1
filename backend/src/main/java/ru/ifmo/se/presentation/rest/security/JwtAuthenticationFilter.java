package ru.ifmo.se.presentation.rest.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.service.JwtService;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Inject
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Optional<String> tokenOpt = extractToken(requestContext);
        if (tokenOpt.isEmpty()) {
            return;
        }

        Optional<DecodedJWT> decodedOpt = jwtService.verifyToken(tokenOpt.get());
        if (decodedOpt.isEmpty()) {
            return;
        }

        DecodedJWT jwt = decodedOpt.get();
        String username = jwtService.extractUsername(jwt);
        Set<String> roles = jwtService.extractRoles(jwt);

        SecurityContext originalContext = requestContext.getSecurityContext();
        boolean isSecure = originalContext != null && originalContext.isSecure();

        requestContext.setSecurityContext(new JwtSecurityContext(username, roles, isSecure));
    }

    private Optional<String> extractToken(ContainerRequestContext requestContext) {
        Cookie cookie = requestContext.getCookies().get(CookieService.ACCESS_TOKEN_COOKIE_NAME);
        if (cookie != null && !cookie.getValue().isBlank()) {
            return Optional.of(cookie.getValue());
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length()).trim();
            if (!token.isBlank()) {
                return Optional.of(token);
            }
        }

        return Optional.empty();
    }

    private static class JwtSecurityContext implements SecurityContext {
        private final String username;
        private final Set<String> roles;
        private final boolean secure;

        JwtSecurityContext(String username, Set<String> roles, boolean secure) {
            this.username = username;
            this.roles = roles;
            this.secure = secure;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> username;
        }

        @Override
        public boolean isUserInRole(String role) {
            return roles != null && roles.contains(role);
        }

        @Override
        public boolean isSecure() {
            return secure;
        }

        @Override
        public String getAuthenticationScheme() {
            return "Bearer";
        }
    }
}
