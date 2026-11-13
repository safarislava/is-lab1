package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.application.usecase.ObjectsUseCase;
import ru.ifmo.se.presentation.dto.request.BatchObjectsCreateRequest;
import ru.ifmo.se.presentation.dto.response.BatchObjectsResponse;
import ru.ifmo.se.presentation.mapper.ObjectsPresentationMapper;


@Path("/objects")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ObjectsResource {
    @Inject
    private ObjectsUseCase objectsUseCase;

    @Inject
    private ObjectsPresentationMapper mapper;

    @POST
    public Response create(@Valid @NotNull BatchObjectsCreateRequest request) {
        ObjectsCreateCommand command = mapper.toCommand(request);
        ObjectsResult result = objectsUseCase.create(command);
        BatchObjectsResponse response = mapper.toResponse(result);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
