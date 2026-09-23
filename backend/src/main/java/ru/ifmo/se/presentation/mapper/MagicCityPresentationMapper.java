package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.result.MagicCityResult;
import ru.ifmo.se.presentation.dto.request.MagicCityCreateRequest;
import ru.ifmo.se.presentation.dto.request.MagicCityUpdateRequest;
import ru.ifmo.se.presentation.dto.response.MagicCityResponse;

@ApplicationScoped
public class MagicCityPresentationMapper {

    public MagicCityCreateCommand toCommand(MagicCityCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new MagicCityCreateCommand(
                request.getName(),
                request.getArea(),
                request.getPopulation(),
                request.getEstablishmentDate(),
                request.getGovernor(),
                request.getCapital(),
                request.getPopulationDensity()
        );
    }

    public MagicCityUpdateCommand toCommand(MagicCityUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return new MagicCityUpdateCommand(
                request.getName(),
                request.getArea(),
                request.getPopulation(),
                request.getEstablishmentDate(),
                request.getGovernor(),
                request.getCapital(),
                request.getPopulationDensity()
        );
    }

    public MagicCityResponse toResponse(MagicCityResult result) {
        if (result == null) {
            return null;
        }
        return new MagicCityResponse(
                result.getId(),
                result.getName(),
                result.getArea(),
                result.getPopulation(),
                result.getEstablishmentDate(),
                result.getGovernor(),
                result.getCapital(),
                result.getPopulationDensity()
        );
    }
}
