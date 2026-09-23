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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.result.MagicCityResult;
import ru.ifmo.se.application.usecase.MagicCityUseCase;
import ru.ifmo.se.presentation.dto.request.MagicCityCreateRequest;
import ru.ifmo.se.presentation.dto.request.MagicCityUpdateRequest;
import ru.ifmo.se.presentation.dto.response.MagicCityResponse;
import ru.ifmo.se.presentation.mapper.MagicCityPresentationMapper;

import java.util.List;

@Path("/cities")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MagicCityResource {
    @Inject
    private MagicCityUseCase magicCityUseCase;

    @Inject
    private MagicCityPresentationMapper magicCityMapper;

    @GET
    public Response getAll() {
        List<MagicCityResponse> cities = magicCityUseCase.getAll().stream()
                .map(magicCityMapper::toResponse)
                .toList();
        return Response.ok(cities).build();
    }

    @GET
    @Path("/{id:\\d+}")
    public Response getById(@PathParam("id") int id) {
        MagicCityResult city = magicCityUseCase.getById(id);
        MagicCityResponse response = magicCityMapper.toResponse(city);
        return Response.ok(response).build();
    }

    @POST
    public Response create(@Valid @NotNull MagicCityCreateRequest request) {
        MagicCityCreateCommand command = magicCityMapper.toCommand(request);
        MagicCityResult created = magicCityUseCase.create(command);
        MagicCityResponse response = magicCityMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id:\\d+}")
    public Response update(@PathParam("id") int id, @Valid @NotNull MagicCityUpdateRequest request) {
        MagicCityUpdateCommand command = magicCityMapper.toCommand(request);
        MagicCityResult updated = magicCityUseCase.update(id, command);
        MagicCityResponse response = magicCityMapper.toResponse(updated);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id:\\d+}")
    public Response delete(@PathParam("id") int id,
                           @QueryParam("replacementCityId") @NotNull Integer replacementCityId) {
        magicCityUseCase.delete(id, replacementCityId);
        return Response.noContent().build();
    }
}
