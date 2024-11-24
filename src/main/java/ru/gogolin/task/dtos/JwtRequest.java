package ru.gogolin.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import ru.gogolin.task.utils.ValidationConstants;

public record JwtRequest(
        @NotBlank(message = "Email is mandatory!")
        @Pattern(regexp = ValidationConstants.REGEXP_VALIDATE_EMAIL, message = "Invalid e-mail address")
        @Schema(defaultValue = "user@email.ru", description = "Email address")
        String email,

        @NotBlank(message = "Password is mandatory!")
        @Schema(defaultValue = "user", description = "Password")
        String password
) {
}
