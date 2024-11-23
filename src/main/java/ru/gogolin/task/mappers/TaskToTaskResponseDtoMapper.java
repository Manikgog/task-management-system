package ru.gogolin.task.mappers;

import org.springframework.stereotype.Component;
import ru.gogolin.task.dtos.TaskResponseDto;
import ru.gogolin.task.dtos.UserDto;
import ru.gogolin.task.entities.Task;

@Component
public class TaskToTaskResponseDtoMapper {

    public TaskResponseDto apply(Task task, UserDto author, UserDto executor) {
        return new TaskResponseDto(
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getName(),
                task.getPriority().getName(),
                author,
                executor
        );
    }
}
