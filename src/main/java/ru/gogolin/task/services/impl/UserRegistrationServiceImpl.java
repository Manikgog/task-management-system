package ru.gogolin.task.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gogolin.task.annotations.LogException;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.mappers.UserMapper;
import ru.gogolin.task.repositories.UsersRepository;
import ru.gogolin.task.services.RoleService;
import ru.gogolin.task.services.UserRegistrationService;
import java.util.List;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private final UsersRepository usersRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UsersRepository usersRepository,
                                   RoleService roleService,
                                   UserMapper userMapper,
                                   PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @LogExecution
    @LogException
    public void createNewUser(RegistrationDto registrationDto) {
        if(!registrationDto.password().equals(registrationDto.confirmPassword())){
            throw new BadRequestException("Passwords don't match");
        }
        if(usersRepository.findByUsername(registrationDto.email()).isPresent()){
            throw new BadRequestException("The user with the specified email already exists");
        }
        User user = userMapper.fromDtoToEntity(registrationDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));
        usersRepository.save(user);
    }

}
