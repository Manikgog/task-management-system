package ru.gogolin.task.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface UserMapper {
    User fromDtoToEntity(RegistrationDto registrationDto);
    RegistrationDto fromEntityToDto(User entity);
}
