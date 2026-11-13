package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.command.LoginCommand;
import ru.ifmo.se.application.dto.command.RegisterCommand;
import ru.ifmo.se.application.dto.result.AuthTokensResult;
import ru.ifmo.se.application.dto.result.UserResult;

public interface AuthUseCase {
    UserResult register(RegisterCommand command);

    AuthTokensResult login(LoginCommand command);

    AuthTokensResult refreshToken(String refreshToken);

    void logout(String refreshToken);

    UserResult getCurrentUser(String username);
}
