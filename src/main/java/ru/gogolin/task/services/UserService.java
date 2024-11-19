package ru.gogolin.task.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.repositories.RoleRepository;
import ru.gogolin.task.repositories.UsersRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;

    public UserService(UsersRepository usersRepository, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<User> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }

    public void createNewUser(User user) {
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        usersRepository.save(user);
    }
}
