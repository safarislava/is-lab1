package ru.ifmo.se.presentation.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.application.repository.UserRepository;
import ru.ifmo.se.persistence.entity.RoleType;
import ru.ifmo.se.presentation.dto.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

@Path("/admin")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AdminResource {

    @Inject
    private UserRepository userRepository;

    @GET
    @Path("/users")
    public Response getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles().stream().map(RoleType::name).collect(Collectors.toSet())
                ))
                .toList();

        return Response.ok(users).build();
    }
}
