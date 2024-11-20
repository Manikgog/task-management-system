package ru.gogolin.task.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.mappers.UserMapper;
import ru.gogolin.task.repositories.UsersRepository;
import java.util.List;

@Service
public class UserRegistrationService {
    private final UsersRepository usersRepository;
    private final UsersRolesService usersRolesService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UsersRepository usersRepository,
                                   UsersRolesService usersRolesService,
                                   RoleService roleService,
                                   UserMapper userMapper,
                                   PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.usersRolesService = usersRolesService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void createNewUser(RegistrationDto registrationDto) {
        if(!registrationDto.password().equals(registrationDto.confirmPassword())){
            throw new BadRequestException("Пароли не совпадают");
        }
        if(usersRepository.findByEmail(registrationDto.email()).isPresent()){
            throw new BadRequestException("Пользователь с указаной почтой уже существует");
        }
        User user = userMapper.fromDtoToEntity(registrationDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));
        User newUser = usersRepository.save(user);
        List<Role> roles = (List)newUser.getRoles();
        for (Role role : roles) {
            usersRolesService.saveUsersRole(newUser.getId(), role.getId());
        }
    }

}
