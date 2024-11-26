package ru.gogolin.task.mappers;

import org.springframework.stereotype.Component;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.UserDto;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.User;
import java.util.function.Function;

@Component
public class UserToUserDtoMapper implements Function<User, UserDto> {

    @LogExecution
    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getUsername(),
                user.getName(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}
