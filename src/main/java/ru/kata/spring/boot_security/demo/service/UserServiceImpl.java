package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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
    @Transactional
    public List<User> index() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User show(int id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void save(User user, String[] roles, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Arrays.stream(roles)
                .map(roleService::findByName)
                .collect(Collectors.toSet()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    @Transactional
    public void update(User updatedUser, String[] newRoles) {
        User oldUser = userRepository.findByName(updatedUser.getName());

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
