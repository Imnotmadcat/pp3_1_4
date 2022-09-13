package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    void addUser(User user);

    List<User> getAll();

    void deleteUser(long id);

    User getById(long id);

    List<Role> roleList();
}
