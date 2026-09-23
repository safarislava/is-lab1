package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.command.RingUpdateCommand;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.application.usecase.RingUseCase;
import ru.ifmo.se.presentation.dto.request.RingCreateRequest;
import ru.ifmo.se.presentation.dto.request.RingUpdateRequest;
import ru.ifmo.se.presentation.dto.response.RingResponse;
import ru.ifmo.se.presentation.mapper.RingPresentationMapper;

import java.util.List;

@Path("/rings")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RingResource {
    @Inject
    private RingUseCase ringUseCase;

    @Inject
    private RingPresentationMapper ringMapper;

    @GET
    public Response getAll() {
        List<RingResponse> rings = ringUseCase.getAll().stream()
                .map(ringMapper::toResponse)
                .toList();
        return Response.ok(rings).build();
    }

    @GET
    @Path("/{id:\\d+}")
    public Response getById(@PathParam("id") int id) {
        RingResult ring = ringUseCase.getById(id);
        RingResponse response = ringMapper.toResponse(ring);
        return Response.ok(response).build();
    }

    @POST
    public Response create(@Valid @NotNull RingCreateRequest request) {
        RingCreateCommand command = ringMapper.toCommand(request);
        RingResult created = ringUseCase.create(command);
        RingResponse response = ringMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id:\\d+}")
    public Response update(@PathParam("id") int id, @Valid @NotNull RingUpdateRequest request) {
        RingUpdateCommand command = ringMapper.toCommand(request);
        RingResult updated = ringUseCase.update(id, command);
        RingResponse response = ringMapper.toResponse(updated);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id:\\d+}")
    public Response delete(@PathParam("id") int id) {
        ringUseCase.delete(id);
        return Response.noContent().build();
    }
}
