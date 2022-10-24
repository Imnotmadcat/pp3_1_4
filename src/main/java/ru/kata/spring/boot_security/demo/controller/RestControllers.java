package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestControllers {

    private final UserDetailsServiceImpl userDetailsServiceimpl;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RestControllers(UserService userService, UserDetailsServiceImpl userDetailsServiceimpl, RoleService roleService) {
        this.userService = userService;
        this.userDetailsServiceimpl = userDetailsServiceimpl;
        this.roleService = roleService;
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(userService.findUserById(user.getId()), HttpStatus.OK);
    }

    @PatchMapping("/admin/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{id}")
    public void deleteUser(@PathVariable("id") User user) {
        userService.deleteUser(user.getId());
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userDetailsServiceimpl.loadUserByUsername(principal.getName()), HttpStatus.OK);
    }
}
