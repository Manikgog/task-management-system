package ru.gogolin.task.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.User;

import static ru.gogolin.task.testdata.TestData.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void fromDtoToEntityTest() {
        RegistrationDto registrationDto = new RegistrationDto(
                USER_EMAIL,
                USER_NAME,
                USER_NAME,
                USER_NAME
        );
        User user = userMapper.fromDtoToEntity(registrationDto);
        Assertions.assertEquals(USER_NAME, user.getName());
        Assertions.assertEquals(USER_EMAIL, user.getUsername());
        Assertions.assertEquals(USER_NAME, user.getPassword());
    }


}
