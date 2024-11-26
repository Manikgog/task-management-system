package ru.gogolin.task.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @LogExecution
    @Mapping(source = "email", target = "username")
    User fromDtoToEntity(RegistrationDto registrationDto);

}
