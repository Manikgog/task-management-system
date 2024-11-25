package ru.gogolin.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

public record CommentDto(

        @NotBlank(message = "Title is mandatory!")
        @Length(max = 150, message = "The title is too long, the max number of symbols is 250")
        @Schema(defaultValue = "Title of task")
        String taskTitle,

        @NotBlank(message = "Comment is mandatory!")
        @Length(max = 250, message = "The comment is too long, the max number of symbols is 250")
        @Schema(defaultValue = "Text of comment", description = "The content of the comment.")
        String comment
) implements Serializable {
}
