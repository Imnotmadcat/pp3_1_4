package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class CreateFirstUsers {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public CreateFirstUsers(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void createTestUsers() {
        roleService.save(new Role("ROLE_ADMIN"));
        roleService.save(new Role("ROLE_USER"));

        String[] adminRoles = {"ROLE_ADMIN", "ROLE_USER"};

        userService.save(new User("admin", "admin", "admin@gmail.com",(byte) 100), adminRoles, "admin");

        String[] userRoles = {"ROLE_USER"};

        userService.save(new User("user", "user", "user@gmail.com",(byte) 100), userRoles, "user");
    }
}
