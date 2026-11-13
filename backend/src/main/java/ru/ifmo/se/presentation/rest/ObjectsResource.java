package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.presentation.dto.request.BatchObjectsCreateRequest;


@Path("/objects")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ObjectsResource {
    @POST
    public Response create(@Valid @NotNull BatchObjectsCreateRequest request) {

    }
}
