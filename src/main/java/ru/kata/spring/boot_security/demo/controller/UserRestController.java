package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

import java.security.Principal;

@RestController
public class UserRestController {
    private final UserDetailsServiceImpl userDetailsServiceimpl;

    @Autowired
    public UserRestController(UserDetailsServiceImpl userDetailsServiceimpl) {
        this.userDetailsServiceimpl = userDetailsServiceimpl;
    }

    @GetMapping( "/user")
    public ModelAndView getUserPage() {
        return new ModelAndView("adminPanel");
    }

    @GetMapping("/api/user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userDetailsServiceimpl.loadUserByUsername(principal.getName()), HttpStatus.OK);
    }
}
