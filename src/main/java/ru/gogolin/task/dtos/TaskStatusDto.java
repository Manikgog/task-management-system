package ru.gogolin.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TaskStatusDto(
        @NotBlank(message = "Title is mandatory!")
        @Length(max = 150, message = "The title is too long, the max number of symbols is 150")
        @Schema(defaultValue = "Title of task.")
        String title,

        @NotBlank(message = "Status is mandatory!")
        @Length(max = 50, message = "The title is too long, the max number of symbols is 50")
        @Schema(defaultValue = "Title of task.", description = "В процессе")
        String status

) {
}
