package ru.ifmo.se.presentation.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.result.ImportOperationResult;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.usecase.ImportHistoryUseCase;
import ru.ifmo.se.application.usecase.ObjectsUseCase;
import ru.ifmo.se.presentation.dto.request.BatchObjectsCreateRequest;
import ru.ifmo.se.presentation.dto.response.BatchObjectsResponse;
import ru.ifmo.se.presentation.dto.response.ImportOperationPageResponse;
import ru.ifmo.se.presentation.mapper.ImportPresentationMapper;
import ru.ifmo.se.presentation.mapper.ObjectsPresentationMapper;

@Path("/objects")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
public class ObjectsResource {
    @Inject
    private ObjectsUseCase objectsUseCase;

    @Inject
    private ObjectsPresentationMapper objectsMapper;

    @Inject
    private ImportHistoryUseCase importHistoryUseCase;

    @Inject
    private ImportPresentationMapper importMapper;

    @POST
    public Response create(BatchObjectsCreateRequest request,
                           @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        ObjectsCreateCommand command = objectsMapper.toCommand(request);
        ObjectsResult result = objectsUseCase.create(command, username);
        BatchObjectsResponse response = objectsMapper.toResponse(result);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/history")
    public Response getHistory(
        @QueryParam("page") @DefaultValue("0") @Min(0) int page,
        @QueryParam("size") @DefaultValue("10") @Min(1) @Max(100) int size,
        @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        boolean isAdmin = securityContext.isUserInRole("ADMIN");

        PageResult<ImportOperationResult> results = importHistoryUseCase.getHistory(username, isAdmin, page, size);
        ImportOperationPageResponse response = importMapper.toPageResponse(results);

        return Response.ok(response).build();
    }
}
