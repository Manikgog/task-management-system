package ru.gogolin.task.services.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.repositories.UsersRepository;

@Service
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findByEmail(String email) {
        return usersRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь '%s' не найден", email)));
    }

    @Override
    @Transactional
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

    public void deleteByEmail(String email) {
        User userToDelete = findByEmail(email);
        usersRepository.delete(userToDelete);
    }
}
