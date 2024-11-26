package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.gogolin.task.annotations.LogException;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.*;
import ru.gogolin.task.entities.Priority;
import ru.gogolin.task.entities.Status;
import ru.gogolin.task.entities.Task;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.exceptions.EntityNotFoundException;
import ru.gogolin.task.exceptions.IsNotExecutorException;
import ru.gogolin.task.mappers.TaskToTaskResponseDtoMapper;
import ru.gogolin.task.mappers.UserToUserDtoMapper;
import ru.gogolin.task.repositories.TaskRepository;
import ru.gogolin.task.services.PriorityService;
import ru.gogolin.task.services.StatusService;
import ru.gogolin.task.services.TaskService;
import java.security.Principal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final UserToUserDtoMapper userToUserDtoMapper;
    private final TaskToTaskResponseDtoMapper taskToTaskDtoMapper;

    @Override
    @LogExecution
    @LogException
    public TaskResponseDto addTask(TaskDto taskDto) {
        if(taskRepository.findByTitle(taskDto.title()).isPresent()){
            throw new BadRequestException(String.format("Task with title %s already exists", taskDto.title()));
        }
        User executor = userService.findByEmail(taskDto.executor());
        User author = userService.findByEmail(taskDto.author());
        Status status = statusService.getStatus(taskDto.status());
        Priority priority = priorityService.getPriority(taskDto.priority());
        Task task = new Task(
                taskDto.title(),
                taskDto.description(),
                status,
                author,
                priority,
                executor
        );
        Task newTask = taskRepository.save(task);
        UserDto authorFromDB = userToUserDtoMapper.apply(newTask.getAuthor());
        UserDto executorFromDB = userToUserDtoMapper.apply(newTask.getExecutor());
        return taskToTaskDtoMapper.apply(newTask, authorFromDB, executorFromDB);
    }

    @Override
    @LogExecution
    @LogException
    public void deleteTask(String title) {
        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new NotFoundException(String.format("The task called %s was not found", title)));
        taskRepository.delete(task);
    }

    @Override
    @LogExecution
    @LogException
    public TaskResponseDto getTask(String title, String email) {
        Task task = getTaskByTitle(title);
        if(task.getAuthor().getUsername().equals(email)) {
            UserDto authorFromDB = userToUserDtoMapper.apply(task.getAuthor());
            UserDto executorFromDB = userToUserDtoMapper.apply(task.getExecutor());
            return taskToTaskDtoMapper.apply(task, authorFromDB, executorFromDB);
        }
        throw new BadRequestException(String.format("The user with email=%s is not the author of the task with the name %s", email, title));
    }

    @Override
    @LogExecution
    public List<TaskResponseDto> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.stream()
                .map(task -> new TaskResponseDto(
                            task.getTitle(),
                            task.getDescription(),
                            task.getStatus().getName(),
                            task.getPriority().getName(),
                            userToUserDtoMapper.apply(task.getAuthor()),
                            userToUserDtoMapper.apply(task.getExecutor())
                    )
                ).toList();
    }

    @Override
    @LogExecution
    public List<TaskResponseDto> getTasksByAuthor(String authorEmail, int page, int size) {
        User author = userService.findByEmail(authorEmail);
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findByAuthor_Username(author.getUsername(), pageable);
        return tasks.stream()
                .map(task -> new TaskResponseDto(
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus().getName(),
                                task.getPriority().getName(),
                                userToUserDtoMapper.apply(task.getAuthor()),
                                userToUserDtoMapper.apply(task.getExecutor())
                        )
                ).toList();
    }


    @Override
    @LogExecution
    public List<TaskResponseDto> getTasksByExecutor(String executorEmail, int page, int size) {
        User executor = userService.findByEmail(executorEmail);
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findByExecutor_Username(executor.getUsername(), pageable);
        return tasks.stream()
                .map(task -> new TaskResponseDto(
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus().getName(),
                                task.getPriority().getName(),
                                userToUserDtoMapper.apply(task.getAuthor()),
                                userToUserDtoMapper.apply(task.getExecutor())
                        )
                ).toList();
    }


    @Override
    @LogExecution
    public List<TaskResponseDto> getTasks(Principal principal,int page, int size) {
        User author = userService.findByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findByAuthor_Username(author.getUsername(), pageable);
        return tasks.stream()
                .map(task -> new TaskResponseDto(
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus().getName(),
                                task.getPriority().getName(),
                                userToUserDtoMapper.apply(task.getAuthor()),
                                userToUserDtoMapper.apply(task.getExecutor())
                        )
                ).toList();
    }

    @Override
    @LogExecution
    @LogException
    public Task getTaskByTitle(String title) {
        return taskRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException(String.format("The task called %s was not found", title)));
    }

    @Override
    @LogExecution
    @LogException
    public TaskResponseDto changeStatus(TaskStatusDto taskDto, Authentication authentication) {
        User executor = userService.findByEmail(authentication.getName());
        Task task = getTaskByTitle(taskDto.title());
        if(executor.equals(task.getExecutor()) ||
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {
            Status status = statusService.getStatus(taskDto.status());
            if(task.getStatus().getName().equals(taskDto.status())) {
                throw new BadRequestException(String.format("This status \"%s\" has already been established", status.getName()));
            }else{
                task.setStatus(status);
                Task newTask = taskRepository.save(task);
                return taskToTaskDtoMapper.apply(newTask,
                        userToUserDtoMapper.apply(newTask.getAuthor()),
                        userToUserDtoMapper.apply(newTask.getExecutor()));
            }
        }
        throw new IsNotExecutorException("You cannot change the status of non-your tasks if you are not an administrator.");
    }

    @Override
    @LogExecution
    public TaskResponseDto changeTaskExecutor(TaskExecutorDto taskDto) {
        Task task = getTaskByTitle(taskDto.title());
        User user = userService.findByEmail(taskDto.email());
        task.setExecutor(user);
        Task newTask = taskRepository.save(task);
        return taskToTaskDtoMapper.apply(newTask,
                userToUserDtoMapper.apply(newTask.getAuthor()),
                userToUserDtoMapper.apply(newTask.getExecutor()));
    }

    @Override
    @LogExecution
    public TaskResponseDto changeTask(TaskPriorityDto taskDto) {
        Task task = getTaskByTitle(taskDto.title());
        Priority priority = priorityService.getPriority(taskDto.priority());
        task.setPriority(priority);
        Task newTask = taskRepository.save(task);
        return taskToTaskDtoMapper.apply(newTask,
                userToUserDtoMapper.apply(newTask.getAuthor()),
                userToUserDtoMapper.apply(newTask.getExecutor()));
    }
}
