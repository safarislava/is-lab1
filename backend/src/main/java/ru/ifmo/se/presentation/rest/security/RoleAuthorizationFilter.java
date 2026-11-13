package ru.ifmo.se.presentation.rest.security;

import jakarta.annotation.Priority;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class RoleAuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        Class<?> resourceClass = resourceInfo.getResourceClass();

        if (method == null || resourceClass == null) {
            return;
        }

        if (isExplicitlyDenied(method, resourceClass)) {
            abortWithForbidden(requestContext);
            return;
        }

        if (isExplicitlyPermitted(method)) {
            return;
        }

        RolesAllowed rolesAllowed = findRolesAllowed(method, resourceClass);
        if (rolesAllowed == null) {
            return;
        }

        checkAuthorization(requestContext, rolesAllowed);
    }

    private void checkAuthorization(ContainerRequestContext requestContext, RolesAllowed rolesAllowed) {
        SecurityContext securityContext = requestContext.getSecurityContext();
        if (securityContext == null || securityContext.getUserPrincipal() == null) {
            abortWithUnauthorized(requestContext);
            return;
        }

        if (!hasRequiredRole(securityContext, rolesAllowed)) {
            abortWithForbidden(requestContext);
        }
    }

    private boolean isExplicitlyDenied(Method method, Class<?> resourceClass) {
        if (method.isAnnotationPresent(DenyAll.class)) {
            return true;
        }
        boolean hasMethodOverride = method.isAnnotationPresent(PermitAll.class)
                || method.isAnnotationPresent(RolesAllowed.class);
        return !hasMethodOverride && resourceClass.isAnnotationPresent(DenyAll.class);
    }

    private boolean isExplicitlyPermitted(Method method) {
        return method.isAnnotationPresent(PermitAll.class);
    }

    private RolesAllowed findRolesAllowed(Method method, Class<?> resourceClass) {
        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            return rolesAllowed;
        }
        return resourceClass.getAnnotation(RolesAllowed.class);
    }

    private boolean hasRequiredRole(SecurityContext securityContext, RolesAllowed rolesAllowed) {
        return Arrays.stream(rolesAllowed.value())
                .anyMatch(securityContext::isUserInRole);
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Требуется авторизация",
                Response.Status.UNAUTHORIZED.getStatusCode()
        );
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

    private void abortWithForbidden(ContainerRequestContext requestContext) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Доступ запрещен: недостаточно прав",
                Response.Status.FORBIDDEN.getStatusCode()
        );
        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
}
