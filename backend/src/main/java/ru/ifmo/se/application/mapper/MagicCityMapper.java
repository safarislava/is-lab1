package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.request.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.request.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.response.MagicCityResponse;
import ru.ifmo.se.persistence.entity.MagicCity;

@ApplicationScoped
public class MagicCityMapper {

    public void updateEntity(MagicCity city, MagicCityUpdateCommand command) {
        if (city == null || command == null) {
            return;
        }
        city.setName(command.getName());
        city.setArea(command.getArea());
        city.setPopulation(command.getPopulation());
        city.setEstablishmentDate(command.getEstablishmentDate());
        city.setGovernor(command.getGovernor());
        city.setCapital(command.getCapital());
        city.setPopulationDensity(command.getPopulationDensity());
    }

    public MagicCity toEntity(MagicCityCreateCommand command) {
        if (command == null) {
            return null;
        }
        MagicCity city = new MagicCity();
        city.setName(command.getName());
        city.setArea(command.getArea());
        city.setPopulation(command.getPopulation());
        city.setEstablishmentDate(command.getEstablishmentDate());
        city.setGovernor(command.getGovernor());
        city.setCapital(command.getCapital());
        city.setPopulationDensity(command.getPopulationDensity());
        return city;
    }

    public MagicCityResponse toResponse(MagicCity city) {
        if (city == null) {
            return null;
        }
        return new MagicCityResponse(
                city.getId(),
                city.getName(),
                city.getArea(),
                city.getPopulation(),
                city.getEstablishmentDate(),
                city.getGovernor(),
                city.getCapital(),
                city.getPopulationDensity()
        );
    }
}
