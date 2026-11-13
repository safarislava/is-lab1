package ru.ifmo.se.presentation.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.application.dto.command.UpdateUserRolesCommand;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.application.usecase.AdminUseCase;
import ru.ifmo.se.presentation.dto.request.UpdateUserRolesRequest;
import ru.ifmo.se.presentation.dto.response.UserResponse;
import ru.ifmo.se.presentation.mapper.AdminPresentationMapper;

import java.util.List;

@Path("/admin")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AdminResource {

    @Inject
    private AdminUseCase adminUseCase;

    @Inject
    private AdminPresentationMapper adminMapper;

    @GET
    @Path("/users")
    public Response getAllUsers() {
        List<UserResponse> users = adminUseCase.getAllUsers().stream()
                .map(adminMapper::toResponse)
                .toList();

        return Response.ok(users).build();
    }

    @PUT
    @Path("/users/{id:\\d+}/roles")
    public Response updateUserRoles(
            @PathParam("id") int id,
            @Valid @NotNull UpdateUserRolesRequest request
    ) {
        UpdateUserRolesCommand command = adminMapper.toCommand(id, request);
        UserResult updated = adminUseCase.updateUserRoles(command);
        UserResponse response = adminMapper.toResponse(updated);

        return Response.ok(response).build();
    }
}
