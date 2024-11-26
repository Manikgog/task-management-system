package ru.gogolin.task.mappers;

import org.springframework.stereotype.Component;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.TaskResponseDto;
import ru.gogolin.task.dtos.UserDto;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.Task;

@Component
public class TaskToTaskResponseDtoMapper {

    @LogExecution
    public TaskResponseDto apply(Task task) {
        UserDto executor;
        if(task.getExecutor() == null){
            executor = null;
        }else{
            executor = new UserDto(task.getExecutor().getUsername(), task.getExecutor().getName(), task.getExecutor().getRoles().stream().map(Role::getName).toList());
        }
        return TaskResponseDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().getName())
                .priority(task.getPriority().getName())
                .author(UserDto.builder()
                        .email(task.getAuthor().getUsername())
                        .name(task.getAuthor().getName())
                        .roles(task.getAuthor().getRoles().stream().map(Role::getName).toList()).build())
                .executor(executor)
                .build();

    }
}
