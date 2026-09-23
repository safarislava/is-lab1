package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
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
import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.query.CreatureSearchQuery;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;
import ru.ifmo.se.presentation.dto.request.BookCreatureCreateRequest;
import ru.ifmo.se.presentation.dto.request.BookCreatureUpdateRequest;
import ru.ifmo.se.presentation.dto.request.CreatureSearchRequest;
import ru.ifmo.se.presentation.dto.response.BookCreatureResponse;
import ru.ifmo.se.presentation.dto.response.PageResponse;
import ru.ifmo.se.presentation.mapper.BookCreaturePresentationMapper;

@Path("/creatures")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookCreatureResource {
    @Inject
    private BookCreatureUseCase bookCreatureUseCase;

    @Inject
    private BookCreaturePresentationMapper bookCreatureMapper;

    @GET
    public Response search(@BeanParam @Valid CreatureSearchRequest request) {
        CreatureSearchQuery query = bookCreatureMapper.toQuery(request);
        PageResult<BookCreatureResult> result = bookCreatureUseCase.search(query);
        PageResponse response = bookCreatureMapper.toResponse(result);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id:\\d+}")
    public Response getById(@PathParam("id") int id) {
        BookCreatureResult result = bookCreatureUseCase.getById(id);
        BookCreatureResponse response = bookCreatureMapper.toResponse(result);
        return Response.ok(response).build();
    }

    @POST
    public Response create(@Valid @NotNull BookCreatureCreateRequest request) {
        BookCreatureCreateCommand command = bookCreatureMapper.toCommand(request);
        BookCreatureResult created = bookCreatureUseCase.create(command);
        BookCreatureResponse response = bookCreatureMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id:\\d+}")
    public Response update(@PathParam("id") int id, @Valid @NotNull BookCreatureUpdateRequest request) {
        BookCreatureUpdateCommand command = bookCreatureMapper.toCommand(request);
        BookCreatureResult updated = bookCreatureUseCase.update(id, command);
        BookCreatureResponse response = bookCreatureMapper.toResponse(updated);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id:\\d+}")
    public Response delete(@PathParam("id") int id) {
        bookCreatureUseCase.delete(id);
        return Response.noContent().build();
    }
}
