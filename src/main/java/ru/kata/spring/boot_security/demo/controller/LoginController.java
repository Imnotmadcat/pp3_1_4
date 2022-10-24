package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loadLoginPage() {
        return "login";
    }

    @GetMapping(value = {"/admin", "/user"})
    public String adminPage() {
        return "index";
    }
}
