package ru.gogolin.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import ru.gogolin.task.utils.ValidationConstants;

public record RegistrationDto(

        @NotBlank(message = "Email is mandatory!")
        @Length(max = 150, message = "The email is too long, the max number of symbols is 150")
        @Pattern(regexp = ValidationConstants.REGEXP_VALIDATE_EMAIL, message = "Invalid e-mail address")
        @Schema(defaultValue = "user@email.ru", description = "Email address")
        String email,

        @NotBlank(message = "Password is mandatory!")
        @Schema(defaultValue = "password", description = "Password")
        String password,

        @NotBlank(message = "Password is mandatory!")
        @Schema(defaultValue = "password", description = "Password")
        String confirmPassword,

        @NotBlank(message = "Name is mandatory!")
        @Length(max = 30, message = "The name is too long, the max number of symbols is 30")
        @Pattern(regexp = ValidationConstants.REGEXP_VALIDATE_NAME, message = "Invalid name")
        @Schema(defaultValue = "Ivan Petrov", description = "User name")
        String name) {
}
