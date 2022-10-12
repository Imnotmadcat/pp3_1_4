package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> allUsers();

    User show(int id);

    void save(User user, String[] roles, String pass);

    void delete(int id);

    User findByEmail(String email);

    void update(User user, String[] roles);
}
