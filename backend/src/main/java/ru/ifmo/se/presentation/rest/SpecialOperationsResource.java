package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.usecase.SpecialOperationsUseCase;
import ru.ifmo.se.presentation.dto.response.AverageResponse;
import ru.ifmo.se.presentation.dto.response.BookCreatureResponse;
import ru.ifmo.se.presentation.dto.response.CountResponse;
import ru.ifmo.se.presentation.mapper.BookCreaturePresentationMapper;

import java.util.List;

@Path("/creatures/special")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecialOperationsResource {
    @Inject
    private SpecialOperationsUseCase specialOperationsUseCase;

    @Inject
    private BookCreaturePresentationMapper bookCreatureMapper;

    @DELETE
    @Path("/by-defense")
    public Response deleteByDefenseLevel(@QueryParam("defense_level") @Positive float defenseLevel) {
        int deletedCount = specialOperationsUseCase.deleteByDefenseLevel(defenseLevel);
        return Response.ok(new CountResponse(deletedCount)).build();
    }

    @GET
    @Path("/avg-defense")
    public Response calculateAverageDefenseLevel() {
        Double avg = specialOperationsUseCase.calculateAverageDefenseLevel();
        return Response.ok(new AverageResponse(avg)).build();
    }

    @GET
    @Path("/attack-less-than")
    public Response findCreaturesWithAttackLevelLessThan(@QueryParam("max_attack_level") @Positive float maxAttackLevel) {
        List<BookCreatureResult> creatures = specialOperationsUseCase.findCreaturesWithAttackLevelLessThan(maxAttackLevel);
        List<BookCreatureResponse> response = creatures.stream()
                .map(bookCreatureMapper::toResponse)
                .toList();
        return Response.ok(response).build();
    }

    @POST
    @Path("/take-rings-from-hobbits")
    public Response takeAllRingsFromHobbits() {
        int updatedCount = specialOperationsUseCase.takeAllRingsFromHobbits();
        return Response.ok(new CountResponse(updatedCount)).build();
    }

    @POST
    @Path("/move-hobbits-to-mordor")
    public Response moveHobbitsWithRingsToMordor() {
        int movedCount = specialOperationsUseCase.moveHobbitsWithRingsToMordor();
        return Response.ok(new CountResponse(movedCount)).build();
    }
}
