package ru.gogolin.task.services.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.repositories.UsersRepository;

@Service
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @LogExecution
    public User findByEmail(String email) {
        return usersRepository.findByUsername(email)
                .orElseThrow(() -> new BadRequestException(String.format("Пользователь c email -> '%s' не найден", email)));
    }

    @Override
    @Transactional
    @LogExecution
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }

    @LogExecution
    public void deleteByEmail(String email) {
        User userToDelete = findByEmail(email);
        usersRepository.delete(userToDelete);
    }
}
