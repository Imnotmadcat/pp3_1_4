package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
public class MainController {

    @GetMapping("/")
    public String MainPage() {
        return "redirect:/login";
    }

    @GetMapping("/auth")
    public String auth() {
        return "index";
    }
}
