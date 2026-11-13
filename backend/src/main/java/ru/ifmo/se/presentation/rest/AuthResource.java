package ru.ifmo.se.presentation.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.application.dto.result.AuthTokensResult;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.application.usecase.AuthUseCase;
import ru.ifmo.se.presentation.dto.request.LoginRequest;
import ru.ifmo.se.presentation.dto.request.RegisterRequest;
import ru.ifmo.se.presentation.dto.response.AuthResponse;
import ru.ifmo.se.presentation.dto.response.UserResponse;
import ru.ifmo.se.presentation.mapper.AuthPresentationMapper;
import ru.ifmo.se.presentation.rest.security.CookieService;

@Path("/auth")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthUseCase authUseCase;

    @Inject
    private AuthPresentationMapper authMapper;

    @Inject
    private CookieService cookieService;

    @POST
    @Path("/register")
    @PermitAll
    public Response register(@Valid @NotNull RegisterRequest request) {
        UserResult created = authUseCase.register(authMapper.toCommand(request));
        UserResponse response = authMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid @NotNull LoginRequest request) {
        AuthTokensResult tokens = authUseCase.login(authMapper.toCommand(request));
        NewCookie accessCookie = cookieService.createAccessTokenCookie(tokens.getAccessToken());
        NewCookie refreshCookie = cookieService.createRefreshTokenCookie(tokens.getRefreshToken());

        AuthResponse response = authMapper.toResponse(tokens, "Успешная аутентификация");

        return Response.ok(response)
                .cookie(accessCookie, refreshCookie)
                .build();
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public Response refresh(@CookieParam(CookieService.REFRESH_TOKEN_COOKIE_NAME) Cookie refreshCookieParam) {
        String token = refreshCookieParam != null ? refreshCookieParam.getValue() : null;
        AuthTokensResult tokens = authUseCase.refreshToken(token);

        NewCookie newAccessCookie = cookieService.createAccessTokenCookie(tokens.getAccessToken());
        NewCookie newRefreshCookie = cookieService.createRefreshTokenCookie(tokens.getRefreshToken());

        AuthResponse response = authMapper.toResponse(tokens, "Токены успешно обновлены");

        return Response.ok(response)
                .cookie(newAccessCookie, newRefreshCookie)
                .build();
    }

    @POST
    @Path("/logout")
    @PermitAll
    public Response logout(@CookieParam(CookieService.REFRESH_TOKEN_COOKIE_NAME) Cookie refreshCookieParam) {
        String token = refreshCookieParam != null ? refreshCookieParam.getValue() : null;
        authUseCase.logout(token);

        NewCookie expiredAccess = cookieService.createExpiredCookie(CookieService.ACCESS_TOKEN_COOKIE_NAME, "/");
        NewCookie expiredRefresh = cookieService.createExpiredCookie(CookieService.REFRESH_TOKEN_COOKIE_NAME, "/api/auth");

        return Response.ok()
                .cookie(expiredAccess, expiredRefresh)
                .build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"USER", "ADMIN"})
    public Response getCurrentUser(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        UserResult user = authUseCase.getCurrentUser(username);
        UserResponse response = authMapper.toResponse(user);
        return Response.ok(response).build();
    }
}
