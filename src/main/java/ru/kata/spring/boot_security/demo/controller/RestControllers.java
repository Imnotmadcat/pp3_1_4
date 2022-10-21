package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestControllers {

    private final UserDetailsServiceImpl userDetailsServiceimpl;
    private final UserService userService;

    @Autowired
    public RestControllers(UserService userService, UserDetailsServiceImpl userDetailsServiceimpl) {
        this.userService = userService;
        this.userDetailsServiceimpl = userDetailsServiceimpl;
    }

    @GetMapping("/admin/users")
    public List<User> showAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/users/{id}")
    public User showUser(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @PostMapping("/admin/users")
    public User createUser(@RequestBody User user) {
        userService.saveUser(user);
        return userDetailsServiceimpl.loadUserByUsername(user.getUsername());
    }

    @PatchMapping("/admin/users/{id}")
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("/admin/users/{id}")
    public void deleteUser(@PathVariable("id") User user) {
        userService.deleteUser(user.getId());
    }

    @GetMapping("/user")
    public  User getCurrentUser(Principal principal) {
        return userDetailsServiceimpl.loadUserByUsername(principal.getName());
    }
}
