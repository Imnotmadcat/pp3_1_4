package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public User show(int id) {
        return userRepository.getById(id);
    }

    @Override
    public void save(User user, String[] roles, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Arrays.stream(roles)
                .map(roleService::findByName)
                .collect(Collectors.toSet()));
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByName(email);
    }

    @Override
    public void update(User updatedUser, String[] newRoles) {
        User oldUser = userRepository.getById(updatedUser.getId());

        Set<Role> newRolesSet = Arrays.stream(newRoles)
                .map(roleService::findByName)
                .collect(Collectors.toSet());

        updatedUser.setRoles(newRolesSet);

        if (!(passwordEncoder.matches(updatedUser.getPassword(), oldUser.getPassword()))
                && (updatedUser.getPassword() != null)
                && !(updatedUser.getPassword().equals(""))) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            updatedUser.setPassword(oldUser.getPassword());
        }
        userRepository.save(updatedUser);
    }
}
