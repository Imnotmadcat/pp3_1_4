package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user, String[] roles, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Arrays.stream(roles)
                .map(roleRepository::findRoleByName)
                .collect(Collectors.toSet()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }

    @Override
    @Transactional
    public void saveUser(User newUser) {
        fixProblemWithRoles(newUser);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void updateUser(User updatedUser) {
        fixProblemWithRoles(updatedUser);

        //если пароль не менялся, не делает перекодировку
        User oldUser = findUserById(updatedUser.getId());
        if (!(updatedUser.getPassword().equals(oldUser.getPassword()))
                && (updatedUser.getPassword() != null)
                && !(updatedUser.getPassword().equals(""))) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            updatedUser.setPassword(oldUser.getPassword());
        }
        userRepository.save(updatedUser);
    }

/*   У меня ошибка при редактировании пользователя и создании нового пользователя.
     Когда я сохраняю таких пользователей, в базу добавляются новые роли,
     с тем же названием, но другим Id,
     приходится по имени переназначать на те, что уже есть в базе,
     есть лучше способ решить эту проблему?
                                                                */
    private void fixProblemWithRoles(User updatedOrNewUser) {
        Set<Role> rolesFromDB = new HashSet<>();
        for (Role role : updatedOrNewUser.getRoles()) {
            rolesFromDB.add(roleRepository.findRoleByName(role.getName()));
        }
        updatedOrNewUser.setRoles(rolesFromDB);
    }
}
