package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    void saveUser(User user, String[] roles, String password);
    void saveUser(User user);

    void deleteUser(int id);

    User findUserById(int id);

    void updateUser(User user, String[] roles);
    void updateUser(User user);
}
