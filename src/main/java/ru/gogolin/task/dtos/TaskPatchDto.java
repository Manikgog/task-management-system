package ru.gogolin.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import ru.gogolin.task.utils.ValidationConstants;

public record TaskPatchDto(
        @NotBlank(message = "Title is mandatory!")
        @Length(max = 150, message = "The title is too long, the max number of symbols is 150")
        @Schema(defaultValue = "Title of task.")
        String title,

        @NotBlank(message = "Status is mandatory!")
        @Length(max = 50, message = "The title is too long, the max number of symbols is 50")
        @Schema(defaultValue = "Title of task.", description = "В процессе")
        String status,

        @NotBlank(message = "Priority is mandatory!")
        @Length(max = 50, message = "The priority is too long, the max number of symbols is 50")
        @Schema(defaultValue = "Priority of task.", description = "Средний")
        String priority,

        @NotBlank(message = "Email of task executor is mandatory!")
        @Length(max = 150, message = "The email is too long, the max number of symbols is 150")
        @Pattern(regexp = ValidationConstants.REGEXP_VALIDATE_EMAIL, message = "Invalid e-mail address")
        @Schema(defaultValue = "user@email.ru", description = "Email address")
        String executor
) {
}
