package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.gogolin.task.dtos.TaskDto;
import ru.gogolin.task.entities.Priority;
import ru.gogolin.task.entities.Status;
import ru.gogolin.task.entities.Task;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.repositories.TaskRepository;
import ru.gogolin.task.services.PriorityService;
import ru.gogolin.task.services.StatusService;
import ru.gogolin.task.services.TaskService;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final StatusService statusService;
    private final PriorityService priorityService;

    @Override
    public void addTask(TaskDto taskDto, String username) {
        User executor = userService.findByEmail(taskDto.executor())
                .orElseThrow(() -> new NotFoundException(String.format("Исполнитель с email %s не найден", taskDto.executor())));
        User author = userService.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(String.format("Автор с email %s не найден", taskDto.executor())));
        Status status = statusService.getStatus(taskDto.status());
        Priority priority = priorityService.getPriority(taskDto.priority());
        Task task = new Task(
                taskDto.title(),
                taskDto.description(),
                status,
                author,
                priority,
                taskDto.comment(),
                executor
        );
        taskRepository.save(task);
    }
}
