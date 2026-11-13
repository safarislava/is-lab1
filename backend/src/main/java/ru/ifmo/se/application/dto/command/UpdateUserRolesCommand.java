package ru.ifmo.se.application.dto.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.RoleType;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRolesCommand {
    @Positive
    private int userId;

    @NotNull
    @NotEmpty
    private Set<RoleType> roles;
}
