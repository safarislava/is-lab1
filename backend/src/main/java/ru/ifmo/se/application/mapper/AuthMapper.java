package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.persistence.entity.RoleType;
import ru.ifmo.se.persistence.entity.User;

import java.util.EnumSet;

@ApplicationScoped
public class AuthMapper {

    public UserResult toResult(User user) {
        if (user == null) {
            return null;
        }
        return new UserResult(
                user.getId(),
                user.getUsername(),
                user.getRoles() != null && !user.getRoles().isEmpty()
                        ? EnumSet.copyOf(user.getRoles())
                        : EnumSet.noneOf(RoleType.class)
        );
    }
}
