package ru.gogolin.task.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gogolin.task.dtos.UserDto;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.User;
import java.util.List;

import static ru.gogolin.task.testdata.TestData.*;

public class UserToUserDtoMapperTest {

    private final UserToUserDtoMapper userToUserDtoMapper = new UserToUserDtoMapper();

    @Test
    public void userToUserDtoTest() {
        User user = createUser();
        UserDto userDto = userToUserDtoMapper.apply(user);
        Assertions.assertEquals(userDto.email(), user.getUsername());
        Assertions.assertEquals(userDto.name(), user.getName());
        Assertions.assertEquals(userDto.roles(), user.getRoles().stream().map(Role::getName).toList());
    }


    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setName(USER_NAME);
        user.setRoles(List.of(new Role(ROLE_USER)));
        return user;
    }

}
